(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $X_TESTRECURSIVO (mut i32) (i32.const 0))
  (global $_ (mut i32) (i32.const 0))
  (global $_t0 (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (global $_t2 (mut i32) (i32.const 0))
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Error: Una funci\c3\b3n no puede llamarse a si misma")
  (data (i32.const 120) "X es igual a 5")
  (data (i32.const 134) "X no es igual a 5")
  (data (i32.const 151) "fin del programa")

  (func $main (export "main")
    global.get $_
    i32.const 6
    i32.add
    global.set $_t0
    global.get $_
    i32.const 1
    i32.mul
    global.set $_t1
    i32.const 1
    i32.const 0
    i32.eq ;; Pila: [B == 0] (0 o 1)
    (if (result i32) 
      (then
        i32.const 0
        i32.const 25
        call $alert_str ;; Muestra el mensaje
        unreachable ;; Termina el programa
      )
      (else
    global.get $_t1
    i32.const 1
        i32.div_u ;; Ejecutar A / B
      )
    ) ;; Fin del IF. El resultado (A/B) está en la pila.
    global.set $_t2
    global.get $_t0
    global.get $_t2
    i32.lt_u ;; Verifica si A < B (Underflow)
    (if 
      (then
        i32.const 25
        i32.const 47
        call $alert_str ;; Muestra el mensaje de error
        unreachable ;; Termina el programa
      )
    ) ;; Fin del IF. La pila queda vacía si hubo error, o inalterada si no.
    global.get $_t0
    global.get $_t2
    i32.sub
    global.set $_t3
    global.get $_t3
    global.set $X_TESTRECURSIVO
(block
(block
    global.get $X_TESTRECURSIVO
    i32.const 5
    i32.eq
    global.set $_t4
    global.get $_t4
    i32.eqz
    br_if 0
    i32.const 120
    i32.const 14
    call $alert_str
br 1) ;; fin block else
    i32.const 134
    i32.const 17
    call $alert_str
br 0) ;; fin block
    i32.const 151
    i32.const 16
    call $alert_str
  )
)
