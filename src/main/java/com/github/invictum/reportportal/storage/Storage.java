package com.github.invictum.reportportal.storage;

import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.SerenityPortalModule;
import com.github.invictum.reportportal.storage.events.Event;
import com.google.inject.Guice;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facility for building and storing test entities hierarchy.
 * Entities are stored in nested tree structure. When tree is build in may be proceed to RP as a sequences of requests to recreate the same structure.
 * It does not push events to RP instantly. A portion of test details (suite) is uploaded to RP.
 * s
 */
public class Storage {

    private static Logger LOG = LoggerFactory.getLogger(Storage.class);
    private TreeNode suite = new TreeNode();
    private ReportPortal portal;

    public Storage() {
        suite.setType(ItemType.SUITE);
        portal = Guice.createInjector(new SerenityPortalModule()).getInstance(ReportPortal.class);
    }

    /**
     * Emit some event to alter storage state.
     *
     * @param event should be proceed.
     */
    public void fire(Event event) {
        event.proceed(findNode(suite, event));
    }

    /**
     * Flush created entities tree recursively to RP facility. And recreates the same structure.
     */
    public void dump() {
        dumpNode(suite, null);

    }

    private TreeNode findNode(TreeNode node, Event event) {
        switch (event.searchMode()) {
            case CHILDREN_FIRST:
                if (!node.getChildren().isEmpty() && node.getChildren().peekFirst().isOpen()) {
                    return findNode(node.getChildren().peekFirst(), event);
                } else if (event.isCompatible(node.getType())) {
                    return node;
                }
                break;
            case PARENT_FIRST:
                if (event.isCompatible(node.getType())) {
                    return node;
                } else if (!node.getChildren().isEmpty() && node.getChildren().peekFirst().isOpen()) {
                    return findNode(node.getChildren().peekFirst(), event);
                }
                break;
        }
        /* Required node not found or closed */
        LOG.debug("Event {}", event);
        LOG.debug("Node {}", node);
        throw new IllegalStateException("Failed to alter node");
    }

    private void dumpNode(TreeNode node, Maybe<String> parent) {
        /* Start */
        StartTestItemRQ start = new StartTestItemRQ();
        start.setName(node.getName());
        start.setType(node.getType().key());
        start.setStartTime(node.getStartTime());
        Maybe<String> id = parent == null ? portal.startTestItem(start) : portal.startTestItem(parent, start);
        /* Emmit all logs */
        for (Message message : node.getMessages()) {
            if (message.getBody() != null) {
                ReportPortal.emitLog(message.getBody(), message.getLevel().toString(), message.getDate());
            } else {
                ReportPortal.emitLog(message.getMessage(), message.getLevel().toString(), message.getDate());
            }
        }
        /* Proceed children */
        while (node.getChildren().peekFirst() != null) {
            dumpNode(node.getChildren().pollFirst(), id);
        }
        /* Finish */
        FinishTestItemRQ finish = new FinishTestItemRQ();
        finish.setStatus(node.getStatus().toString());
        finish.setEndTime(node.getEndTime());
        portal.finishTestItem(id, finish);
    }
}
