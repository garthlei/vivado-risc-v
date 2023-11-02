# SDIO
set_property -dict { PACKAGE_PIN AT37  IOSTANDARD LVCMOS18 IOB TRUE } [get_ports { sdio_clk }];
set_property -dict { PACKAGE_PIN AT38  IOSTANDARD LVCMOS18 IOB TRUE } [get_ports { sdio_cmd }];
set_property -dict { PACKAGE_PIN BA43  IOSTANDARD LVCMOS18 IOB TRUE } [get_ports { sdio_dat[0] }];
set_property -dict { PACKAGE_PIN AY43  IOSTANDARD LVCMOS18 IOB TRUE } [get_ports { sdio_dat[1] }];
set_property -dict { PACKAGE_PIN AW44  IOSTANDARD LVCMOS18 IOB TRUE } [get_ports { sdio_dat[2] }];
set_property -dict { PACKAGE_PIN AW43  IOSTANDARD LVCMOS18 IOB TRUE } [get_ports { sdio_dat[3] }];
# set_property -dict { PACKAGE_PIN BA39  IOSTANDARD LVCMOS18 } [get_ports { sdio_cd }];
# Note: card detect does not seem to work on custom daughter board