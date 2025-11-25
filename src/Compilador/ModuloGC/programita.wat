(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $DUMMY_PROGRAMAPAR (mut i32) (i32.const 0))
  (global $_t2_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $ESPAR2_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $_t_t3_PROGRAMAPAR_PROGRAMAPAR (mut i32) (i32.const 0))
  (global $NUM_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $MITAD_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $_t_t1_PROGRAMAPAR_ESPAR_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $_t_t2_PROGRAMAPAR_ESPAR_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $X_PROGRAMAPAR (mut i32) (i32.const 0))
  (global $CHECK_PROGRAMAPAR (mut i32) (i32.const 0))
  (global $RECONSTRUIDO_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $X_PROGRAMAPAR_ESPAR_H (mut i32) (i32.const 0))
  (global $_t3_PROGRAMAPAR (mut i32) (i32.const 0))
  (global $_t1_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $_lock_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_lock_PROGRAMAPAR_ESPAR_H (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t0 (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (global $_t2 (mut i32) (i32.const 0))
  (global $_t_t1_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (global $_t_t2_PROGRAMAPAR_ESPAR (mut i32) (i32.const 0))
  (global $_t5 (mut i32) (i32.const 0))
  (global $_t6 (mut i32) (i32.const 0))
  (global $_t_t3_PROGRAMAPAR (mut i32) (i32.const 0))
  (global $_t7 (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Error: Una funci\c3\b3n no puede llamarse a si misma")
  (data (i32.const 120) "Es par")
  (data (i32.const 126) "Es impar")

  (func $main (export "main")
    i32.const 10
    global.set $X_PROGRAMAPAR
    i32.const 0
    global.set $CHECK_PROGRAMAPAR
    global.get $X_PROGRAMAPAR
    global.set $NUM_PROGRAMAPAR_ESPAR
    call $PROGRAMAPAR_ESPAR
    global.set $_t6
    global.get $_t6
    global.set $_t_t3_PROGRAMAPAR
    global.get $ESPAR2_PROGRAMAPAR_ESPAR
    global.set $CHECK_PROGRAMAPAR
    global.get $_t_t3_PROGRAMAPAR
    global.set $DUMMY_PROGRAMAPAR
    (block
        (block
            global.get $CHECK_PROGRAMAPAR
            i32.const 1
            i32.eq
            global.set $_t7
            global.get $_t7
            i32.eqz
            br_if 0
            i32.const 120
            i32.const 6
            call $alert_str
            br 1
            ) ;; fin block else
        i32.const 126
        i32.const 8
        call $alert_str
        br 0
        ) ;; fin block
  )

  (func $PROGRAMAPAR_ESPAR (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PROGRAMAPAR_ESPAR
        i32.const 1
        i32.ne ;; Compara candado != 1
        br_if 0 ;; Si candado != 1, saltar (función inactiva, OK)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PROGRAMAPAR_ESPAR ;; Set Candado a 1
    (block 
        i32.const 2
        i32.const 0
        i32.eq ;; Pila: [B == 0] (0 o 1)
        i32.eqz ;;Invertir condicion
        br_if 0 ;; Si B == 0, salta al IF
        i32.const 0
        i32.const 25
        call $alert_str ;; Muestra el mensaje
        unreachable ;; Termina el programa
    )
    global.get $NUM_PROGRAMAPAR_ESPAR
    i32.const 2
        i32.div_u ;; Ejecutar A / B
    global.set $_t0
    global.get $_t0
    global.set $MITAD_PROGRAMAPAR_ESPAR
    global.get $MITAD_PROGRAMAPAR_ESPAR
    i32.const 2
    i32.mul
    global.set $_t1
    i32.const 45
    global.set $X_PROGRAMAPAR_ESPAR_H
    call $PROGRAMAPAR_ESPAR_H
    global.set $_t2
    global.get $_t2
    global.set $_t_t1_PROGRAMAPAR_ESPAR
    global.get $_t1
    global.get $_t_t1_PROGRAMAPAR_ESPAR
    i32.add
    global.set $_t3
    global.get $_t3
    global.set $RECONSTRUIDO_PROGRAMAPAR_ESPAR
    i32.const 67
    global.set $NUM_PROGRAMAPAR_ESPAR
    call $PROGRAMAPAR_ESPAR
    global.set $_t4
    global.get $_t4
    global.set $_t_t2_PROGRAMAPAR_ESPAR
    global.get $ESPAR2_PROGRAMAPAR_ESPAR
    global.set $MITAD_PROGRAMAPAR_ESPAR
    global.get $_t_t2_PROGRAMAPAR_ESPAR
    global.set $RECONSTRUIDO_PROGRAMAPAR_ESPAR
    (block
        (block
            global.get $RECONSTRUIDO_PROGRAMAPAR_ESPAR
            global.get $NUM_PROGRAMAPAR_ESPAR
            i32.eq
            global.set $_t5
            global.get $_t5
            i32.eqz
            br_if 0
            i32.const 1
            global.set $ESPAR2_PROGRAMAPAR_ESPAR
            br 1
            ) ;; fin block else
        i32.const 0
        global.set $ESPAR2_PROGRAMAPAR_ESPAR
        br 0
        ) ;; fin block
    i32.const 0
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_PROGRAMAPAR_ESPAR ;; Set Candado a 0
    return
  )

  (func $PROGRAMAPAR_ESPAR_H (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PROGRAMAPAR_ESPAR_H
        i32.const 1
        i32.ne ;; Compara candado != 1
        br_if 0 ;; Si candado != 1, saltar (función inactiva, OK)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PROGRAMAPAR_ESPAR_H ;; Set Candado a 1
    global.get $X_PROGRAMAPAR_ESPAR_H
    call $alert_i32
    i32.const 0
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_PROGRAMAPAR_ESPAR_H ;; Set Candado a 0
    return
  )
)
