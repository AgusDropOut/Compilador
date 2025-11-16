(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $_t_t1_MAIN_F_MAIN_F (mut i32) (i32.const 0))
  (global $TEST_MAIN_F_G (mut i32) (i32.const 0))
  (global $_t2_MAIN (mut i32) (i32.const 0))
  (global $_t_t2_MAIN_MAIN (mut i32) (i32.const 0))
  (global $-7_887800e_00 (mut i32) (i32.const 0))
  (global $_t1_MAIN_F (mut i32) (i32.const 0))
  (global $C_MAIN (mut i32) (i32.const 0))
  (global $D_MAIN (mut i32) (i32.const 0))
  (global $PRUEBA_MAIN_F (mut i32) (i32.const 0))
  (global $_t0 (mut i32) (i32.const 0))
  (global $_ret_MAIN_F_G (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (global $_t_t1_MAIN_F (mut i32) (i32.const 0))
  (global $_ret_MAIN_F (mut i32) (i32.const 0))
  (global $_t2 (mut i32) (i32.const 0))
  (global $_t_t2_MAIN (mut i32) (i32.const 0))
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (global $_t5 (mut i32) (i32.const 0))
  (global $_t6 (mut i32) (i32.const 0))
  (global $_t7 (mut i32) (i32.const 0))
  (data (i32.const 0) "Hola")

  (func $main (export "main")
    i32.const 78
    global.set $C_MAIN
    global.get $C_MAIN
    global.set $PRUEBA_MAIN_F
    call $MAIN_F
    global.set $_t2
    global.get $_ret_MAIN_F
    global.set $_t_t2_MAIN
    global.get $_t_t2_MAIN
    i32.const 65
    i32.add
    global.set $_t3
    global.get $-7_887800e_00
    global.set $_t4
    global.get $_t4
    i32.const 6
    i32.div_u
    global.set $_t5
    global.get $_t5
    global.set $_t6
    global.get $_t3
    global.get $_t6
    i32.sub
    global.set $_t7
    global.get $_t7
    global.set $D_MAIN
    global.get $D_MAIN
    call $alert_i32
  )

  (func $MAIN_F (result i32)
    global.get $PRUEBA_MAIN_F
    call $alert_i32
    global.get $PRUEBA_MAIN_F
    global.set $TEST_MAIN_F_G
    call $MAIN_F_G
    global.set $_t1
    global.get $_ret_MAIN_F_G
    global.set $_t_t1_MAIN_F
    global.get $_t_t1_MAIN_F
    global.set $PRUEBA_MAIN_F
    i32.const 0
    i32.const 4
    call $alert_str
    i32.const 0
    global.set $_ret_MAIN_F
    ;; jmp fin_MAIN_F
    i32.const 0
    return
  )

  (func $MAIN_F_G (result i32)
    global.get $TEST_MAIN_F_G
    i32.const 10
    i32.add
    global.set $_t0
    global.get $_t0
    global.set $TEST_MAIN_F_G
    global.get $TEST_MAIN_F_G
    call $alert_i32
    global.get $TEST_MAIN_F_G
    global.set $_ret_MAIN_F_G
    ;; jmp fin_MAIN_F_G
    i32.const 0
    return
  )
)
