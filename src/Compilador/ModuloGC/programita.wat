(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $TOTAL_PRUEBADEFUEGO (mut i32) (i32.const 0))
  (global $I_PRUEBADEFUEGO (mut i32) (i32.const 0))
  (global $J_PRUEBADEFUEGO (mut i32) (i32.const 0))
  (global $_t0 (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (global $_t2 (mut i32) (i32.const 0))
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (global $_t5 (mut i32) (i32.const 0))
  (global $_t6 (mut i32) (i32.const 0))
  (global $_t7 (mut i32) (i32.const 0))
  (global $_t8 (mut i32) (i32.const 0))
  (global $_t9 (mut i32) (i32.const 0))
  (global $_t10 (mut i32) (i32.const 0))
  (global $_t11 (mut i32) (i32.const 0))
  (global $_t12 (mut i32) (i32.const 0))
  (global $_t13 (mut i32) (i32.const 0))
  (global $_t14 (mut i32) (i32.const 0))
  (global $_t15 (mut i32) (i32.const 0))
  (data (i32.const 0) "Error: Division por cero.")
  (data (i32.const 25) "Error: Resultado de resta negativo (underflow).")
  (data (i32.const 72) "Error: Una funci\c3\b3n no puede llamarse a si misma")

  (func $main (export "main")
    i32.const 0
    global.set $TOTAL_PRUEBADEFUEGO
    i32.const 0
    global.set $I_PRUEBADEFUEGO
    (block $break_W0
    (loop $loop_W0
    global.get $I_PRUEBADEFUEGO
    i32.const 6
    i32.lt_u
    global.set $_t0
    global.get $_t0
    i32.eqz
    br_if $break_W0
    (block
        (block
            global.get $I_PRUEBADEFUEGO
            i32.const 3
            i32.lt_u
            global.set $_t1
            global.get $_t1
            i32.eqz
            br_if 0
            (block
                (block
                    global.get $I_PRUEBADEFUEGO
                    i32.const 0
                    i32.eq
                    global.set $_t2
                    global.get $_t2
                    i32.eqz
                    br_if 0
                    global.get $TOTAL_PRUEBADEFUEGO
                    i32.const 1
                    i32.add
                    global.set $_t3
                    global.get $_t3
                    global.set $TOTAL_PRUEBADEFUEGO
                    br 1
                    ) ;; fin block else
                i32.const 0
                global.set $J_PRUEBADEFUEGO
                (block $break_W1
                (loop $loop_W1
                global.get $J_PRUEBADEFUEGO
                global.get $I_PRUEBADEFUEGO
                i32.lt_u
                global.set $_t4
                global.get $_t4
                i32.eqz
                br_if $break_W1
                global.get $TOTAL_PRUEBADEFUEGO
                i32.const 10
                i32.add
                global.set $_t5
                global.get $_t5
                global.set $TOTAL_PRUEBADEFUEGO
                global.get $J_PRUEBADEFUEGO
                i32.const 1
                i32.add
                global.set $_t6
                global.get $_t6
                global.set $J_PRUEBADEFUEGO
                    br $loop_W1
                    ) ;; fin loop
                    ) ;; fin block while
                    br 0
                    ) ;; fin block
                br 1
                ) ;; fin block else
            global.get $I_PRUEBADEFUEGO
            global.set $J_PRUEBADEFUEGO
            (block $break_W2
            (loop $loop_W2
            global.get $J_PRUEBADEFUEGO
            i32.const 0
            i32.gt_u
            global.set $_t7
            global.get $_t7
            i32.eqz
            br_if $break_W2
            (block
                (block
                    global.get $J_PRUEBADEFUEGO
                    i32.const 4
                    i32.eq
                    global.set $_t8
                    global.get $_t8
                    i32.eqz
                    br_if 0
                    global.get $TOTAL_PRUEBADEFUEGO
                    i32.const 100
                    i32.add
                    global.set $_t9
                    global.get $_t9
                    global.set $TOTAL_PRUEBADEFUEGO
                    br 1
                    ) ;; fin block else
                global.get $TOTAL_PRUEBADEFUEGO
                i32.const 2
                i32.add
                global.set $_t10
                global.get $_t10
                global.set $TOTAL_PRUEBADEFUEGO
                br 0
                ) ;; fin block
            (block 
                global.get $J_PRUEBADEFUEGO
                i32.const 1
                i32.lt_u ;; Verifica si A < B (Underflow)
                i32.eqz 
                br_if 0 
                i32.const 25
                i32.const 47
                call $alert_str ;; Muestra el mensaje de error
                unreachable ;; Termina el programa
            )
            global.get $J_PRUEBADEFUEGO
            i32.const 1
            i32.sub
            global.set $_t11
            global.get $_t11
            global.set $J_PRUEBADEFUEGO
                br $loop_W2
                ) ;; fin loop
                ) ;; fin block while
                (block
                    (block
                        global.get $I_PRUEBADEFUEGO
                        i32.const 5
                        i32.eq
                        global.set $_t12
                        global.get $_t12
                        i32.eqz
                        br_if 0
                        global.get $TOTAL_PRUEBADEFUEGO
                        i32.const 1000
                        i32.add
                        global.set $_t13
                        global.get $_t13
                        global.set $TOTAL_PRUEBADEFUEGO
                        br 1
                        ) ;; fin block else
                    global.get $TOTAL_PRUEBADEFUEGO
                    i32.const 0
                    i32.add
                    global.set $_t14
                    global.get $_t14
                    global.set $TOTAL_PRUEBADEFUEGO
                    br 0
                    ) ;; fin block
                br 0
                ) ;; fin block
            global.get $I_PRUEBADEFUEGO
            i32.const 1
            i32.add
            global.set $_t15
            global.get $_t15
            global.set $I_PRUEBADEFUEGO
                br $loop_W0
                ) ;; fin loop
                ) ;; fin block while
                global.get $TOTAL_PRUEBADEFUEGO
                call $alert_i32
  )
)
