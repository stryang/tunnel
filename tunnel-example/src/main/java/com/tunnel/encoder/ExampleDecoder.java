package com.tunnel.encoder;

import com.tunnel.support.coder.DefaultDecoder;

/**
 * @author leo
 * @since 2025/4/27
 */
public class ExampleDecoder extends DefaultDecoder {

    @Override
    protected boolean isWarp() {
        return false;
    }

}
