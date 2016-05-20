
package app.cn.aiyouv.www.download;

/**
 * download state enum
 * 
 * @author offbye@gmail.com
 */
public enum DownloadState {
    /**
     * init
     */
    INITIALIZE,

    /**
     * downloading
     */
    DOWNLOADING,
    /**
     * download failed, the reason may be network error, file io error etc.
     */
    FAILED,
    /**
     * download finished
     */
    FINISHED,

    /**
     * download paused
     */
    PAUSE
}
