package com.idreamsky.permission.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: colby
 * @Date: 2018/12/16 10:17
 */
@Setter
@Getter
public class TestVo {

    @NotBlank
    private String msg;

    @NotNull
    @Range(max = 10, min = 0)
    private Integer id;

    private List<String> str;
}
