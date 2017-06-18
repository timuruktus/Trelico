package ru.timuruktus.trelico.Markers;

public class DownloadError extends Throwable {

    public String errorInfo;

    public DownloadError(String errorInfo){
        this.errorInfo = errorInfo;
    }

    @Override
    public String getMessage() {
        return "Error while downloading. More info: " + errorInfo;
    }
}
