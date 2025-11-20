(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $CONT_TESTRECURSIVO_G (mut i32) (i32.const 0))
  (global $RESULTADO_TESTRECURSIVO (mut i32) (i32.const 0))
  (global $_t2_TESTRECURSIVO (mut i32) (i32.const 0))
  (global $TEMPVALOR_TESTRECURSIVO_G (mut i32) (i32.const 0))
  (global $GLOBALB_TESTRECURSIVO (mut i32) (i32.const 0))
  (global $LOCALF_TESTRECURSIVO_F (mut i32) (i32.const 0))
  (global $VALOR_TESTRECURSIVO_F (mut i32) (i32.const 0))
  (global $LOCALG_TESTRECURSIVO_G (mut i32) (i32.const 0))
  (global $_t1_TESTRECURSIVO_F (mut i32) (i32.const 0))
  (global $_t_t2_TESTRECURSIVO_TESTRECURSIVO (mut i32) (i32.const 0))
  (global $GLOBALA_TESTRECURSIVO (mut i32) (i32.const 0))
  (global $A_TESTRECURSIVO_G (mut i32) (i32.const 0))
  (global $_t_t1_TESTRECURSIVO_F_TESTRECURSIVO_F (mut i32) (i32.const 0))
  (global $_lock_TESTRECURSIVO_G (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_lock__lambda1_TESTRECURSIVO_G (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t0 (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (global $_t2 (mut i32) (i32.const 0))
  (global $_lock_TESTRECURSIVO_F (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (global $_t_t1_TESTRECURSIVO_F (mut i32) (i32.const 0))
  (global $_t5 (mut i32) (i32.const 0))
  (global $_t6 (mut i32) (i32.const 0))
  (global $_t_t2_TESTRECURSIVO (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Error: Una funci\c3\b3n no puede llamarse a si misma")
  (data (i32.const 120) "Hola")
  (data (i32.const 124) "F: Caso Base")
  (data (i32.const 136) "Resultado de la recursividad:")
  (data (i32.const 165) "Fin del test")

  (func $main (export "main")
    i32.const 10
    global.set $GLOBALA_TESTRECURSIVO
    i32.const 7
    global.set $GLOBALB_TESTRECURSIVO
    global.get $GLOBALA_TESTRECURSIVO
    global.set $VALOR_TESTRECURSIVO_F
    call $TESTRECURSIVO_F
    global.set $_t6
    global.get $_t6
    global.set $_t_t2_TESTRECURSIVO
    global.get $_t_t2_TESTRECURSIVO
    global.set $RESULTADO_TESTRECURSIVO
    i32.const 136
    i32.const 29
    call $alert_str
    global.get $RESULTADO_TESTRECURSIVO
    call $alert_i32
    i32.const 165
    i32.const 12
    call $alert_str
  )

  (func $TESTRECURSIVO_G (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    global.get $_lock_TESTRECURSIVO_G
    i32.const 1
    i32.eq ;; Pila: [Candado == 1]
    (if 
      (then
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
      )
    ) ;; Fin IF de check
    i32.const 1
    global.set $_lock_TESTRECURSIVO_G ;; Set Candado a 1
    global.get $GLOBALB_TESTRECURSIVO
    global.set $LOCALG_TESTRECURSIVO_G
    i32.const 34
    global.set $A_TESTRECURSIVO_G
    call $_lambda1_TESTRECURSIVO_G
(block
(block
    global.get $LOCALG_TESTRECURSIVO_G
    i32.const 45
    i32.lt_u
    global.set $_t1
    global.get $_t1
    i32.eqz
    br_if 0
    i32.const 120
    i32.const 4
    call $alert_str
br 1) ;; fin block else
    global.get $CONT_TESTRECURSIVO_G
    global.set $TEMPVALOR_TESTRECURSIVO_G
br 0) ;; fin block
    global.get $LOCALG_TESTRECURSIVO_G
    global.get $CONT_TESTRECURSIVO_G
    i32.add
    global.set $_t2
    global.get $_t2
    global.set $LOCALG_TESTRECURSIVO_G
    i32.const 4
    return
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_TESTRECURSIVO_G ;; Set Candado a 0
  )

  (func $_lambda1_TESTRECURSIVO_G 
    ;; Bloqueo de Recursion Directa (Runtime Check)
    global.get $_lock__lambda1_TESTRECURSIVO_G
    i32.const 1
    i32.eq ;; Pila: [Candado == 1]
    (if 
      (then
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
      )
    ) ;; Fin IF de check
    i32.const 1
    global.set $_lock__lambda1_TESTRECURSIVO_G ;; Set Candado a 1
    global.get $A_TESTRECURSIVO_G
    i32.const 1
    i32.add
    global.set $_t0
    global.get $_t0
    global.set $A_TESTRECURSIVO_G
    global.get $A_TESTRECURSIVO_G
    call $alert_i32
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock__lambda1_TESTRECURSIVO_G ;; Set Candado a 0
  )

  (func $TESTRECURSIVO_F (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    global.get $_lock_TESTRECURSIVO_F
    i32.const 1
    i32.eq ;; Pila: [Candado == 1]
    (if 
      (then
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
      )
    ) ;; Fin IF de check
    i32.const 1
    global.set $_lock_TESTRECURSIVO_F ;; Set Candado a 1
(block
    global.get $VALOR_TESTRECURSIVO_F
    global.get $GLOBALB_TESTRECURSIVO
    i32.lt_u
    global.set $_t3
    global.get $_t3
    i32.eqz
    br_if 0
    i32.const 124
    i32.const 12
    call $alert_str
    global.get $VALOR_TESTRECURSIVO_F
    return
br 0) ;; fin block
    global.get $VALOR_TESTRECURSIVO_F
    global.set $CONT_TESTRECURSIVO_G
    call $TESTRECURSIVO_G
    global.set $_t4
    global.get $_t4
    global.set $_t_t1_TESTRECURSIVO_F
    global.get $_t_t1_TESTRECURSIVO_F
    global.set $LOCALF_TESTRECURSIVO_F
    global.get $LOCALF_TESTRECURSIVO_F
    global.get $VALOR_TESTRECURSIVO_F
    i32.add
    global.set $_t5
    global.get $_t5
    global.set $LOCALF_TESTRECURSIVO_F
    global.get $LOCALF_TESTRECURSIVO_F
    return
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_TESTRECURSIVO_F ;; Set Candado a 0
  )
)
