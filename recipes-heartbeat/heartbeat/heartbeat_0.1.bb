# SPDX-License-Identifier: MIT
# SPDX-Author: Roman Koch <koch.romam@gmail.com>
# SPDX-Copyright: 2024 Roman Koch <koch.romam@gmail.com>

SUMMARY = "Simple Distributet Application Example"
DESCRIPTION = "First test with embedded applications on multiple modules"

LICENSE = "MIT"
# bitbake -e | grep ^COMMON_LICENSE_DIR
# md5sum LICENSE
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

python do_display_banner() {
    bb.plain("***********************************************");
    bb.plain("*    Simple Application Autostart Example     *");
    bb.plain("***********************************************");
}

addtask display_banner before do_build

# Hier wird das externe Application-Repository eingebunden

#SRC_URI = "git://github.com/emblincram/distrap.git;branch=main;protocol=ssh"
SRC_URI = "git:///mnt/ssd/work/heartbeat;branch=main;protocol=file"
SRC_URI += "file://heartbeat.service"

SRCREV = "9fbcdd49b39674950f05404ad8f7b7818616a481"
## scheint buggy zu sein, später: "SRCREV = ${AUTOREV}" oder "SRCREV = "HEAD""
PV = "1.0"
S = "${WORKDIR}/git"

DEPENDS = "cmake"

inherit cmake systemd

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/heartbeat ${D}${bindir}/heartbeat

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/heartbeat.service ${D}${systemd_system_unitdir}/heartbeat.service
}

FILES:${PN} += " \
    ${bindir}/heartbeat \
    ${systemd_system_unitdir}/heartbeat.service \
"

SYSTEMD_SERVICE:${PN} = "heartbeat.service"
SYSTEMD_AUTO_ENABLE = "enable"
