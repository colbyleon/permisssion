package com.idreamsky.permission.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @Author: colby
 * @Date: 2018/12/22 16:51
 */
@Slf4j
@Data
@Builder
@AllArgsConstructor
public class Mail {

    private String subject;

    private String message;

    private Set<String> receivers;
}
