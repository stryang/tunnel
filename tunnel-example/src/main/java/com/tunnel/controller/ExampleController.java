package com.tunnel.controller;

import com.tunnel.TunnelClientExample;
import com.tunnel.model.resp.CountryResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author leo
 * @since 2025/4/27
 */
@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    private final TunnelClientExample tunnelClientExample;

    @RequestMapping("/getCountry")
    public List<CountryResp> get() {
        return tunnelClientExample.get();
    }

}
