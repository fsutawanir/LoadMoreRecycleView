package nu.aing.loadmorerecycleview.task;

import java.util.List;

/**
 * @author Fanny Irawan Sutawanir (fannyirawans@gmail.com)
 */
public interface IEvent {

    List<String> getData();

    void closeDialog();
}
