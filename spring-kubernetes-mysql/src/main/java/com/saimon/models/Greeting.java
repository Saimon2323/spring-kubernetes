package com.saimon.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Muhammad Saimon
 * @since Apr 4/12/21 5:02 PM
 */

@AllArgsConstructor
@Getter
public class Greeting {

    private final Long id;
    private final String content;

}
