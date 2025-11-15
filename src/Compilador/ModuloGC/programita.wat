(module
  (import "env" "alert_i32" (func $alert_i32 (param i32)))
  (import "env" "alert_str" (func $alert_str (param i32 i32)))
  (memory (export "memory") 1)
  (global $_7UL (mut i32) (i32.const 0))
  (global $B_MAIN (mut i32) (i32.const 0))
  (global $U_MAIN (mut i32) (i32.const 0))
  (global $I_MAIN (mut i32) (i32.const 0))
  (global $O_MAIN (mut i32) (i32.const 0))
  (global $_0UL (mut i32) (i32.const 0))
  (global $C_MAIN (mut i32) (i32.const 0))
  (global $_5UL (mut i32) (i32.const 0))
  (global $_6UL (mut i32) (i32.const 0))
  (global $aux0 (mut i32) (i32.const 0))
  (global $aux1 (mut i32) (i32.const 0))
  (data (i32.const 0) "Hola")

  (func $main (export "main")
    i32.const 0
    global.set $C_MAIN
    i32.const 5
    global.set $B_MAIN
    i32.const 6
    global.set $I_MAIN
    i32.const 7
    global.set $O_MAIN
    i32.const 0
    i32.const 4
    call $alert_str
    global.get $C_MAIN
    global.get $B_MAIN
    i32.add
    global.set $aux0
    global.get $aux0
    call $alert_i32
    global.get $I_MAIN
    global.get $O_MAIN
    i32.mul
    global.set $aux1
    global.get $aux1
    call $alert_i32
  )
)
