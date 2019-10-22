package com.demo.common.utils;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * uuid
 *
 * @author gc
 */
public class IdGenerate implements SessionIdGenerator {

    /**
     * 生成UUID, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public Serializable generateId(Session session) {
        return IdGenerate.uuid();
    }
}