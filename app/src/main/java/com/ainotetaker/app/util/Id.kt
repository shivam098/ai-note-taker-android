package com.ainotetaker.app.util

import kotlin.random.Random

/** Dependency-free unique id, good enough for local records. */
fun newId(prefix: String): String =
    "$prefix-${System.currentTimeMillis().toString(36)}-${Random.nextInt(0, 1_000_000).toString(36)}"
