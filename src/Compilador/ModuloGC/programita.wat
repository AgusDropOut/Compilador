(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $Y_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $Z_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $X_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $_t0 (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Fin del programa")

  (func $main (export "main")
    i32.const 5
    global.set $X_PROGRAMAFUNCIONAL
    i32.const 0
    global.set $Y_PROGRAMAFUNCIONAL
    i32.const 4
    global.set $Z_PROGRAMAFUNCIONAL
    global.get $X_PROGRAMAFUNCIONAL
    global.get $Z_PROGRAMAFUNCIONAL
    i32.lt_u ;; Verifica si A < B (Underflow)
    (if 
      (then
        i32.const 25
        i32.const 47
        call $alert_str ;; Muestra el mensaje de error
        unreachable ;; Termina el programa
      )
    ) ;; Fin del IF. La pila queda vacía si hubo error, o inalterada si no.
    global.get $X_PROGRAMAFUNCIONAL
    global.get $Z_PROGRAMAFUNCIONAL
    i32.sub
    global.set $_t0
    global.get $_t0
    global.set $X_PROGRAMAFUNCIONAL
    global.get $X_PROGRAMAFUNCIONAL
    call $alert_i32
    i32.const 72
    i32.const 16
    call $alert_str
  )
)
