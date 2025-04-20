package com.conviteplus.conviteplus.util;

import java.util.UUID;

public class Util {

    public static String gerarCodigoConvite() {
        return UUID.randomUUID().toString(); // Gera um código único
    }
}
