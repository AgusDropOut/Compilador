(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $_t2_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $VALOR_PROGRAMAFUNCIONAL_CALCMODULO (mut i32) (i32.const 0))
  (global $_t_t1_PROGRAMAFUNCIONAL_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $Y_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $_t_t2_PROGRAMAFUNCIONAL_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $Z_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $X_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $_t1_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $_t0 (mut i32) (i32.const 0))
  (global $_t1 (mut i32) (i32.const 0))
  (global $_t2 (mut i32) (i32.const 0))
  (global $_t3 (mut i32) (i32.const 0))
  (global $_t4 (mut i32) (i32.const 0))
  (global $_t5 (mut i32) (i32.const 0))
  (global $_t_t1_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $_t6 (mut i32) (i32.const 0))
  (global $_t7 (mut i32) (i32.const 0))
  (global $_t8 (mut i32) (i32.const 0))
  (global $_t9 (mut i32) (i32.const 0))
  (global $_t10 (mut i32) (i32.const 0))
  (global $_t11 (mut i32) (i32.const 0))
  (global $_t12 (mut i32) (i32.const 0))
  (global $_t13 (mut i32) (i32.const 0))
  (global $_t14 (mut i32) (i32.const 0))
  (global $_t_t2_PROGRAMAFUNCIONAL (mut i32) (i32.const 0))
  (global $_t15 (mut i32) (i32.const 0))
  (global $_t16 (mut i32) (i32.const 0))
  (global $_t17 (mut i32) (i32.const 0))

  (func $main (export "main")
    i32.const 10
    global.set $X_PROGRAMAFUNCIONAL
    i32.const 2
    global.set $Y_PROGRAMAFUNCIONAL
    i32.const 0
    global.set $Z_PROGRAMAFUNCIONAL
(block
(block
    global.get $Y_PROGRAMAFUNCIONAL
    i32.const 2
    i32.eq
    global.set $_t3
    global.get $_t3
    i32.eqz
    br_if 0
    (block $break_W0
    (loop $loop_W0
    global.get $Y_PROGRAMAFUNCIONAL
    i32.const 5
    i32.lt_u
    global.set $_t4
    global.get $_t4
    i32.eqz
    br_if $break_W0
(block
    global.get $X_PROGRAMAFUNCIONAL
    global.set $VALOR_PROGRAMAFUNCIONAL_CALCMODULO
    call $PROGRAMAFUNCIONAL_CALCMODULO
    global.set $_t5
    global.get $_t5
    global.set $_t_t1_PROGRAMAFUNCIONAL
    global.get $_t_t1_PROGRAMAFUNCIONAL
    i32.const 1
    i32.eq
    global.set $_t6
    global.get $_t6
    i32.eqz
    br_if 0
    global.get $X_PROGRAMAFUNCIONAL
    i32.const 1
    i32.add
    global.set $_t7
    global.get $_t7
    global.set $X_PROGRAMAFUNCIONAL
br 0) ;; fin block
    (block $break_W1
    (loop $loop_W1
    global.get $Z_PROGRAMAFUNCIONAL
    i32.const 3
    i32.lt_u
    global.set $_t8
    global.get $_t8
    i32.eqz
    br_if $break_W1
    global.get $X_PROGRAMAFUNCIONAL
    i32.const 2
    i32.add
    global.set $_t9
    global.get $_t9
    global.set $X_PROGRAMAFUNCIONAL
    global.get $Z_PROGRAMAFUNCIONAL
    i32.const 1
    i32.add
    global.set $_t10
    global.get $_t10
    global.set $Z_PROGRAMAFUNCIONAL
    br $loop_W1
    ) ;; fin loop
    ) ;; fin block while
    i32.const 0
    global.set $Z_PROGRAMAFUNCIONAL
    global.get $Y_PROGRAMAFUNCIONAL
    i32.const 1
    i32.add
    global.set $_t11
    global.get $_t11
    global.set $Y_PROGRAMAFUNCIONAL
    br $loop_W0
    ) ;; fin loop
    ) ;; fin block while
br 1) ;; fin block else
    i32.const 999
    global.set $X_PROGRAMAFUNCIONAL
br 0) ;; fin block
(block
    global.get $X_PROGRAMAFUNCIONAL
    i32.const 30
    i32.gt_u
    global.set $_t12
    global.get $_t12
    i32.eqz
    br_if 0
    (block $break_W2
    (loop $loop_W2
    global.get $X_PROGRAMAFUNCIONAL
    i32.const 60
    i32.lt_u
    global.set $_t13
    global.get $_t13
    i32.eqz
    br_if $break_W2
(block
(block
    global.get $X_PROGRAMAFUNCIONAL
    global.set $VALOR_PROGRAMAFUNCIONAL_CALCMODULO
    call $PROGRAMAFUNCIONAL_CALCMODULO
    global.set $_t14
    global.get $_t14
    global.set $_t_t2_PROGRAMAFUNCIONAL
    global.get $_t_t2_PROGRAMAFUNCIONAL
    i32.const 0
    i32.eq
    global.set $_t15
    global.get $_t15
    i32.eqz
    br_if 0
    global.get $X_PROGRAMAFUNCIONAL
    i32.const 5
    i32.add
    global.set $_t16
    global.get $_t16
    global.set $X_PROGRAMAFUNCIONAL
br 1) ;; fin block else
    global.get $X_PROGRAMAFUNCIONAL
    i32.const 3
    i32.add
    global.set $_t17
    global.get $_t17
    global.set $X_PROGRAMAFUNCIONAL
br 0) ;; fin block
    br $loop_W2
    ) ;; fin loop
    ) ;; fin block while
br 0) ;; fin block
    global.get $X_PROGRAMAFUNCIONAL
    call $alert_i32
  )

  (func $PROGRAMAFUNCIONAL_CALCMODULO (result i32)
    global.get $VALOR_PROGRAMAFUNCIONAL_CALCMODULO
    i32.const 2
    i32.div_u
    global.set $_t0
    global.get $_t0
    i32.const 2
    i32.mul
    global.set $_t1
    global.get $VALOR_PROGRAMAFUNCIONAL_CALCMODULO
    global.get $_t1
    i32.sub
    global.set $_t2
    global.get $_t2
    return
  )
)
