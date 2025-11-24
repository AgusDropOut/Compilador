(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $_t_t1_PRUEBARIGUROSA_GENERARVALOR_PRUEBARIGUROSA_GENERARVALOR (mut i32) (i32.const 0))
  (global $_t_t5_PRUEBARIGUROSA_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_t_t4_PRUEBARIGUROSA_H_PRUEBARIGUROSA_H (mut i32) (i32.const 0))
  (global $CALCULO2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2 (mut i32) (i32.const 0))
  (global $DESCARTE_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $A_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $C_PRUEBARIGUROSA_H (mut i32) (i32.const 0))
  (global $_t3_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_t_t2_PRUEBARIGUROSA_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_t4_PRUEBARIGUROSA_H (mut i32) (i32.const 0))
  (global $_t2_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_t1_PRUEBARIGUROSA_GENERARVALOR (mut i32) (i32.const 0))
  (global $SEMILLA_PRUEBARIGUROSA_GENERARVALOR (mut i32) (i32.const 0))
  (global $INICIO_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR (mut i32) (i32.const 0))
  (global $CALCULO_PRUEBARIGUROSA_GENERARVALOR (mut i32) (i32.const 0))
  (global $SEMILLA2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2 (mut i32) (i32.const 0))
  (global $CERO_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $CONTENEDOR2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2 (mut i32) (i32.const 0))
  (global $_t5_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_t_t3_PRUEBARIGUROSA_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $SALIDA_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_lock_PRUEBARIGUROSA_GENERARVALOR (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t0 (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (global $_t2 (mut i32) (i32.const 0))
  (global $_lock_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2 (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (global $_t_t1_PRUEBARIGUROSA_GENERARVALOR (mut i32) (i32.const 0))
  (global $_t5 (mut i32) (i32.const 0))
  (global $_t6 (mut i32) (i32.const 0))
  (global $_t7 (mut i32) (i32.const 0))
  (global $_t_t2_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_t8 (mut i32) (i32.const 0))
  (global $_t_t3_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (global $_lock_PRUEBARIGUROSA_H (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t9 (mut i32) (i32.const 0))
  (global $_t_t4_PRUEBARIGUROSA_H (mut i32) (i32.const 0))
  (global $_t10 (mut i32) (i32.const 0))
  (global $_t_t5_PRUEBARIGUROSA (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Error: Una funci\c3\b3n no puede llamarse a si misma")

  (func $main (export "main")
    i32.const 8
    global.set $INICIO_PRUEBARIGUROSA
    i32.const 9999
    global.set $SALIDA_PRUEBARIGUROSA
    i32.const 9999
    global.set $CERO_PRUEBARIGUROSA
    global.get $CERO_PRUEBARIGUROSA
    global.set $CERO_PRUEBARIGUROSA
    (block 
        global.get $CERO_PRUEBARIGUROSA
        i32.const 0
        i32.eq ;; Pila: [B == 0] (0 o 1)
        i32.eqz ;;Invertir condicion
        br_if 0 ;; Si B == 0, salta al IF
        i32.const 0
        i32.const 25
        call $alert_str ;; Muestra el mensaje
        unreachable ;; Termina el programa
    )
    global.get $SALIDA_PRUEBARIGUROSA
    global.get $CERO_PRUEBARIGUROSA
        i32.div_u ;; Ejecutar A / B
    global.set $_t6
    global.get $_t6
    global.set $SALIDA_PRUEBARIGUROSA
    global.get $INICIO_PRUEBARIGUROSA
    global.set $SEMILLA_PRUEBARIGUROSA_GENERARVALOR
    call $PRUEBARIGUROSA_GENERARVALOR
    global.set $_t7
    global.get $_t7
    global.set $_t_t2_PRUEBARIGUROSA
    global.get $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR
    global.set $SALIDA_PRUEBARIGUROSA
    global.get $_t_t2_PRUEBARIGUROSA
    global.set $DESCARTE_PRUEBARIGUROSA
    global.get $SALIDA_PRUEBARIGUROSA
    call $alert_i32
    global.get $DESCARTE_PRUEBARIGUROSA
    call $alert_i32
    i32.const 20
    global.set $INICIO_PRUEBARIGUROSA
    global.get $INICIO_PRUEBARIGUROSA
    global.set $SEMILLA_PRUEBARIGUROSA_GENERARVALOR
    call $PRUEBARIGUROSA_GENERARVALOR
    global.set $_t8
    global.get $_t8
    global.set $_t_t3_PRUEBARIGUROSA
    global.get $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR
    global.set $SALIDA_PRUEBARIGUROSA
    global.get $_t_t3_PRUEBARIGUROSA
    global.set $DESCARTE_PRUEBARIGUROSA
    global.get $SALIDA_PRUEBARIGUROSA
    call $alert_i32
    i32.const 56
    global.set $C_PRUEBARIGUROSA_H
    call $PRUEBARIGUROSA_H
    global.set $_t10
    global.get $_t10
    global.set $_t_t5_PRUEBARIGUROSA
    global.get $_t_t5_PRUEBARIGUROSA
    global.set $A_PRUEBARIGUROSA
  )

  (func $PRUEBARIGUROSA_GENERARVALOR (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PRUEBARIGUROSA_GENERARVALOR
        i32.const 1
        i32.eq ;; Pila: [Candado == 1]
        br_if 0 ;; Si candado != 1, saltar (continuar normalmente)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PRUEBARIGUROSA_GENERARVALOR ;; Set Candado a 1
    global.get $SEMILLA_PRUEBARIGUROSA_GENERARVALOR
    i32.const 10
    i32.mul
    global.set $_t0
    global.get $_t0
    global.set $CALCULO_PRUEBARIGUROSA_GENERARVALOR
    global.get $CALCULO_PRUEBARIGUROSA_GENERARVALOR
    i32.const 5
    i32.add
    global.set $_t1
    global.get $_t1
    global.set $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR
    (block
        global.get $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR
        i32.const 100
        i32.gt_u
        global.set $_t2
        global.get $_t2
        i32.eqz
        br_if 0
        i32.const 100
        global.set $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR
        br 0
        ) ;; fin block
    global.get $SEMILLA_PRUEBARIGUROSA_GENERARVALOR
    global.set $SEMILLA2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2
    call $PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2
    global.set $_t4
    global.get $_t4
    global.set $_t_t1_PRUEBARIGUROSA_GENERARVALOR
    global.get $CONTENEDOR2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2
    global.set $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR
    global.get $_t_t1_PRUEBARIGUROSA_GENERARVALOR
    i32.const 15
    i32.add
    global.set $_t5
    global.get $_t5
    global.set $CONTENEDOR_PRUEBARIGUROSA_GENERARVALOR
    global.get $CALCULO_PRUEBARIGUROSA_GENERARVALOR
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_PRUEBARIGUROSA_GENERARVALOR ;; Set Candado a 0
    return
  )

  (func $PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2 (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2
        i32.const 1
        i32.eq ;; Pila: [Candado == 1]
        br_if 0 ;; Si candado != 1, saltar (continuar normalmente)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2 ;; Set Candado a 1
    global.get $SEMILLA2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2
    i32.const 20
    i32.mul
    global.set $_t3
    global.get $_t3
    global.set $CALCULO2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2
    global.get $CALCULO2_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_PRUEBARIGUROSA_GENERARVALOR_GENERARVALOR2 ;; Set Candado a 0
    return
  )

  (func $PRUEBARIGUROSA_H (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PRUEBARIGUROSA_H
        i32.const 1
        i32.eq ;; Pila: [Candado == 1]
        br_if 0 ;; Si candado != 1, saltar (continuar normalmente)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PRUEBARIGUROSA_H ;; Set Candado a 1
    i32.const 67
    global.set $C_PRUEBARIGUROSA_H
    call $PRUEBARIGUROSA_H
    global.set $_t9
    global.get $_t9
    global.set $_t_t4_PRUEBARIGUROSA_H
    global.get $_t_t4_PRUEBARIGUROSA_H
    global.set $C_PRUEBARIGUROSA_H
    i32.const 0
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_PRUEBARIGUROSA_H ;; Set Candado a 0
    return
  )
)
