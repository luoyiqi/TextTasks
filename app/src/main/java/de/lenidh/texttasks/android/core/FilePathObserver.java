package de.lenidh.texttasks.android.core;

import java.util.TimerTask;
import java.util.Timer;
import java.io.File;

/**
 * Monitors file and directory pathnames to fire an event after the referenced
 * file or directory was changed.
 * The monitoring method is polling.
 * This class is thread-safe.
 * <p>
 * A change is either one of the following filesystem events:
 * <ul>
 * <li>The file or directory represented by the monitored pathname is modified,
 * deleted or moved.
 * <li>A file or directory is created at or moved to the monitored pathname.
 * </ul>
 * <p class="bug">
 * Some events are not fired for files and directories last modified at epoch
 * (00:00:00 GMT, January 1, 1970).
 *
 * @see File
 */
abstract class FilePathObserver {

    private final File monitored;
    private final long period;
    private final Object syncLock = new Object();

    // time that the file was last modified when last checked
    private long timestamp;
    private Timer timer;

    /**
     * Creates a new FilePathObserver for the specified pathname.
     * Monitoring does not start on creation.
     * <p>
     * In the long run, the frequency of polling will generally be slightly
     * lower than the reciprocal of the specified period (assuming the system
     * clock underlying Object.wait(long) is accurate).
     *
     * @param pathname the pathname
     * @param period time in milliseconds between polls
     * @throws IllegalArgumentException if period is <= 0
     * @throws NullPointerException if pathname is null
     *
     * @see #startWatching()
     */
    public FilePathObserver(File pathname, long period) {
        if(pathname == null)
            throw new NullPointerException("Pathname must not be null.");
        if(period <= 0)
            throw new IllegalArgumentException("Period must be positive.");

        this.monitored = pathname;
        this.period = period;
        this.timestamp = this.monitored.lastModified();
    }

    /**
     * The event handler, which must be implemented by subclasses.
     * This method is invoked on the polling thread.
     */
    public abstract void onChange();

    /**
     * Start watching for changes.
     * If monitoring is already started, this call has no effect.
     *
     * @see #stopWatching()
     */
    public void startWatching() {
        synchronized(this.syncLock) {
            if(this.timer == null) {
                this.timer = new Timer();
                this.timer.schedule(new CheckModifiedTask(), 0, period);
            }
        }
    }

    /**
     * Stop watching for changes.
     * If monitoring is already stopped, this call has no effect.
     *
     * @see #startWatching()
     */
    public void stopWatching() {
        synchronized(this.syncLock) {
            if(this.timer != null) {
                this.timer.cancel();
                this.timer = null;
            }
        }
    }

    private class CheckModifiedTask extends TimerTask {
        public void run() {
            long lastModified = monitored.lastModified();
            if(lastModified != timestamp) {
                timestamp = lastModified;
                onChange();
            }
        }
    }
}

