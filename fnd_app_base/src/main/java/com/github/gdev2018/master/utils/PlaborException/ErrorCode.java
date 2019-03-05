/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.utils.PlaborException;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by RET on 21.03.2018.
 */

public final class ErrorCode
{
    private final int code;
    private final String name;
    private final ErrorType type;

    @JsonCreator
    public ErrorCode(
            @JsonProperty("code") int code,
            @JsonProperty("name") String name,
            @JsonProperty("type") ErrorType type)
    {
        if (code < 0) {
            throw new IllegalArgumentException("code is negative");
        }
        this.code = code;
        this.name = requireNonNull(name, "name is null");
        this.type = requireNonNull(type, "type is null");
    }

    @JsonProperty
    public int getCode()
    {
        return code;
    }

    @JsonProperty
    public String getName()
    {
        return name;
    }

    @JsonProperty
    public ErrorType getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return name + ":" + code;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ErrorCode that = (ErrorCode) obj;
        return Objects.equals(this.code, that.code);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(code);
    }
}
