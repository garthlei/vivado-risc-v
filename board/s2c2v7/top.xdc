set_property BITSTREAM.CONFIG.UNUSEDPIN pulldown [current_design]
set_property BITSTREAM.GENERAL.COMPRESS true [current_design]
set_property BITSTREAM.CONFIG.SPI_BUSWIDTH 4 [current_design]
set_property BITSTREAM.CONFIG.CONFIGRATE 50 [current_design]
set_property BITSTREAM.CONFIG.SPI_FALL_EDGE YES [current_design]
set_property CONFIG_VOLTAGE 1.8 [current_design]
set_property CFGBVS GND [current_design]

# SPI device parameters
set tco_max 6.5
set tco_min 2
set tsu 2.5
set th 2
set tdata_trace_delay_max 0.5
set tdata_trace_delay_min 0.5
set tclk_trace_delay_max 0.5
set tclk_trace_delay_min 0.5

# Reset
set_property -dict { PACKAGE_PIN AP31 IOSTANDARD LVCMOS18 } [get_ports reset]

# SYSCLK 200MHz
set_property -dict { PACKAGE_PIN D9 IOSTANDARD LVDS } [get_ports sys_diff_clock_clk_p]
set_property -dict { PACKAGE_PIN C9 IOSTANDARD LVDS } [get_ports sys_diff_clock_clk_n]
set_property CLOCK_DEDICATED_ROUTE BACKBONE [get_nets -of_objects [get_pins -hier clk_wiz_0/clk_in1]]
create_clock -period 5.000 -name clk100_1 -waveform {0.000 2.500} [get_ports sys_diff_clock_clk_p]

