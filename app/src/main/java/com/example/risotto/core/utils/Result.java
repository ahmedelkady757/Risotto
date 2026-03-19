package com.example.risotto.core.utils;


public class Result<T> {

    // ── State enum ───

    public enum Status { SUCCESS, ERROR, LOADING }

    // ── Fields ───

    private final Status  status;
    private final T       data;
    private final String  message;
    private final Throwable error;


    private Result(Status status, T data, String message, Throwable error) {
        this.status  = status;
        this.data    = data;
        this.message = message;
        this.error   = error;
    }

    // ── Factories ───

    public static <T> Result<T> success(T data) {
        return new Result<>(Status.SUCCESS, data, null, null);
    }

    public static <T> Result<T> error(String message, Throwable error) {
        return new Result<>(Status.ERROR, null, message, error);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(Status.ERROR, null, message, null);
    }

    public static <T> Result<T> loading() {
        return new Result<>(Status.LOADING, null, null, null);
    }

    // ── State checks ───

    public boolean isSuccess() { return status == Status.SUCCESS; }
    public boolean isError()   { return status == Status.ERROR;   }
    public boolean isLoading() { return status == Status.LOADING; }

    // ── Getters ───

    public Status    getStatus()  { return status;  }
    public T         getData()    { return data;    }
    public String    getMessage() { return message; }
    public Throwable getError()   { return error;   }

    // ── Debug ───

    @Override
    public String toString() {
        return "Result{"
                + "status=" + status
                + (data    != null ? ", data="    + data    : "")
                + (message != null ? ", message=" + message : "")
                + (error   != null ? ", error="   + error.getMessage() : "")
                + '}';
    }
}