(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_float" (func $alert_float (param f64)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $X_TRUNC (mut i32) (i32.const 0))
  (global $Y_TRUNC (mut i32) (i32.const 0))
  (global $_t0 (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Error: Una funci\c3\b3n no puede llamarse a si misma")
  (data (i32.const 120) "inicio programa")

  (func $main (export "main")
    i32.const 120
    i32.const 15
    call $alert_str
    f64.const 1.100000e+00
    call $alert_float
    i32.const 1
    global.set $X_TRUNC
    f64.const 1.200000e+00
    i32.trunc_f64_u
    global.set $_t0
    global.get $_t0
    global.get $X_TRUNC
    i32.add
    global.set $_t1
    global.get $_t1
    global.set $Y_TRUNC
    global.get $Y_TRUNC
    call $alert_i32
  )
)
