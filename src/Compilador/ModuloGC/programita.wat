(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $BASEDOS_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t1_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $SALIDA_PROGRAMAMAE_PROCESAR (mut i32) (i32.const 0))
  (global $A_PROGRAMAMAE_SUMAR (mut i32) (i32.const 0))
  (global $TOPE_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $BASEUNA_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t2_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t_t1_PROGRAMAMAE_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $LIMITE_PROGRAMAMAE_PROCESAR (mut i32) (i32.const 0))
  (global $_t_t2_PROGRAMAMAE_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t_t3_PROGRAMAMAE_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t_t4_PROGRAMAMAE_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $BOLSAGLOBAL_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t3_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $CONTADOR_PROGRAMAMAE_PROCESAR (mut i32) (i32.const 0))
  (global $DUMMY_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t4_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $N_PROGRAMAMAE_CUADRADO (mut i32) (i32.const 0))
  (global $RESFINAL_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $B_PROGRAMAMAE_SUMAR (mut i32) (i32.const 0))
  (global $VAL_PROGRAMAMAE_PROCESAR__lambda1 (mut i32) (i32.const 0))
  (global $_lock_PROGRAMAMAE_CUADRADO (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t0 (mut i32) (i32.const 0))
  (global $_lock_PROGRAMAMAE_SUMAR (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t1 (mut i32) (i32.const 0))
  (global $_lock_PROGRAMAMAE_PROCESAR (mut i32) (i32.const 0)) ;; Candado de Recursion
  (global $_t2 (mut i32) (i32.const 0))
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (global $_t5 (mut i32) (i32.const 0))
  (global $_t6 (mut i32) (i32.const 0))
  (global $_t7 (mut i32) (i32.const 0))
  (global $_t8 (mut i32) (i32.const 0))
  (global $_t_t1_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t9 (mut i32) (i32.const 0))
  (global $_t_t2_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t10 (mut i32) (i32.const 0))
  (global $_t_t3_PROGRAMAMAE (mut i32) (i32.const 0))
  (global $_t11 (mut i32) (i32.const 0))
  (global $_t_t4_PROGRAMAMAE (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Error: Una funci\c3\b3n no puede llamarse a si misma")

  (func $main (export "main")
    i32.const 0
    global.set $BOLSAGLOBAL_PROGRAMAMAE
        i32.const 3
        global.set $BASEUNA_PROGRAMAMAE
        i32.const 4
        global.set $BASEDOS_PROGRAMAMAE
        i32.const 0
        global.set $RESFINAL_PROGRAMAMAE
        i32.const 1111
        call $alert_i32
        global.get $BASEUNA_PROGRAMAMAE
        global.set $N_PROGRAMAMAE_CUADRADO
        call $PROGRAMAMAE_CUADRADO
        global.set $_t8
        global.get $_t8
        global.set $_t_t1_PROGRAMAMAE
        global.get $_t_t1_PROGRAMAMAE
        global.set $A_PROGRAMAMAE_SUMAR
        global.get $BASEDOS_PROGRAMAMAE
        global.set $N_PROGRAMAMAE_CUADRADO
        call $PROGRAMAMAE_CUADRADO
        global.set $_t9
        global.get $_t9
        global.set $_t_t2_PROGRAMAMAE
        global.get $_t_t2_PROGRAMAMAE
        global.set $B_PROGRAMAMAE_SUMAR
        call $PROGRAMAMAE_SUMAR
        global.set $_t10
        global.get $_t10
        global.set $_t_t3_PROGRAMAMAE
        global.get $_t_t3_PROGRAMAMAE
        global.set $TOPE_PROGRAMAMAE
        global.get $TOPE_PROGRAMAMAE
        call $alert_i32
        global.get $TOPE_PROGRAMAMAE
        global.set $LIMITE_PROGRAMAMAE_PROCESAR
        call $PROGRAMAMAE_PROCESAR
        global.set $_t11
        global.get $_t11
        global.set $_t_t4_PROGRAMAMAE
        global.get $SALIDA_PROGRAMAMAE_PROCESAR
        global.set $RESFINAL_PROGRAMAMAE
        global.get $_t_t4_PROGRAMAMAE
        global.set $DUMMY_PROGRAMAMAE
        i32.const 9999
        call $alert_i32
        global.get $RESFINAL_PROGRAMAMAE
        call $alert_i32
  )

  (func $PROGRAMAMAE_CUADRADO (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PROGRAMAMAE_CUADRADO
        i32.const 1
        i32.ne ;; Compara candado != 1
        br_if 0 ;; Si candado != 1, saltar (función inactiva, OK)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PROGRAMAMAE_CUADRADO ;; Set Candado a 1
    global.get $N_PROGRAMAMAE_CUADRADO
    global.get $N_PROGRAMAMAE_CUADRADO
    i32.mul
    global.set $_t0
    global.get $_t0
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_PROGRAMAMAE_CUADRADO ;; Set Candado a 0
    return
  )

  (func $PROGRAMAMAE_SUMAR (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PROGRAMAMAE_SUMAR
        i32.const 1
        i32.ne ;; Compara candado != 1
        br_if 0 ;; Si candado != 1, saltar (función inactiva, OK)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PROGRAMAMAE_SUMAR ;; Set Candado a 1
    global.get $A_PROGRAMAMAE_SUMAR
    global.get $B_PROGRAMAMAE_SUMAR
    i32.add
    global.set $_t1
    global.get $_t1
    ;; Liberacion de Candado de Recursion
    i32.const 0
    global.set $_lock_PROGRAMAMAE_SUMAR ;; Set Candado a 0
    return
  )

  (func $PROGRAMAMAE_PROCESAR (result i32)
    ;; Bloqueo de Recursion Directa (Runtime Check)
    (block
        global.get $_lock_PROGRAMAMAE_PROCESAR
        i32.const 1
        i32.ne ;; Compara candado != 1
        br_if 0 ;; Si candado != 1, saltar (función inactiva, OK)
        i32.const 72
        i32.const 48
        call $alert_str ;; Muestra el error
        unreachable ;; Termina el programa
    )
    i32.const 1
    global.set $_lock_PROGRAMAMAE_PROCESAR ;; Set Candado a 1
    global.get $LIMITE_PROGRAMAMAE_PROCESAR
    global.set $CONTADOR_PROGRAMAMAE_PROCESAR
    (block $break_W0
    (loop $loop_W0
    global.get $CONTADOR_PROGRAMAMAE_PROCESAR
    i32.const 0
    i32.gt_u
    global.set $_t2
    global.get $_t2
    i32.eqz
    br_if $break_W0
    global.get $CONTADOR_PROGRAMAMAE_PROCESAR
    global.set $VAL_PROGRAMAMAE_PROCESAR__lambda1
    call $_lambda1_PROGRAMAMAE_PROCESAR
    (block 
        global.get $CONTADOR_PROGRAMAMAE_PROCESAR
        i32.const 1
        i32.lt_u ;; Verifica si A < B (Underflow)
        i32.eqz 
        br_if 0 
        i32.const 25
        i32.const 47
        call $alert_str ;; Muestra el mensaje de error
        unreachable ;; Termina el programa
    )
    global.get $CONTADOR_PROGRAMAMAE_PROCESAR
    i32.const 1
    i32.sub
    global.set $_t7
    global.get $_t7
    global.set $CONTADOR_PROGRAMAMAE_PROCESAR
        br $loop_W0
        ) ;; fin loop
        ) ;; fin block while
        global.get $BOLSAGLOBAL_PROGRAMAMAE
        global.set $SALIDA_PROGRAMAMAE_PROCESAR
        i32.const 0
        ;; Liberacion de Candado de Recursion
        i32.const 0
        global.set $_lock_PROGRAMAMAE_PROCESAR ;; Set Candado a 0
        return
  )

  (func $_lambda1_PROGRAMAMAE_PROCESAR 
    (block
        (block 
            i32.const 5
            i32.const 0
            i32.eq ;; Pila: [B == 0] (0 o 1)
            i32.eqz ;;Invertir condicion
            br_if 0 ;; Si B == 0, salta al IF
            i32.const 0
            i32.const 25
            call $alert_str ;; Muestra el mensaje
            unreachable ;; Termina el programa
        )
        global.get $VAL_PROGRAMAMAE_PROCESAR__lambda1
        i32.const 5
            i32.div_u ;; Ejecutar A / B
        global.set $_t3
        global.get $_t3
        i32.const 5
        i32.mul
        global.set $_t4
        global.get $_t4
        global.get $VAL_PROGRAMAMAE_PROCESAR__lambda1
        i32.eq
        global.set $_t5
        global.get $_t5
        i32.eqz
        br_if 0
        global.get $BOLSAGLOBAL_PROGRAMAMAE
        global.get $VAL_PROGRAMAMAE_PROCESAR__lambda1
        i32.add
        global.set $_t6
        global.get $_t6
        global.set $BOLSAGLOBAL_PROGRAMAMAE
        global.get $VAL_PROGRAMAMAE_PROCESAR__lambda1
        call $alert_i32
        br 0
        ) ;; fin block
  )
)
