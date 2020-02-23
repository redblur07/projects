#!/bin/sh

pacat \
                --device=alsa_output.pci-0000_00_1b.0.analog-stereo.monitor \
                --rate=48000 \
                --record | \
cvlc \
                - \
                --file-caching=500 \
                --quiet \
                -I dummy \
                ':sout=#rtp{late=100,raw,caching=50,sdp=rtsp://:8554/test}' ':rtsp-caching=50' ':sout-all' ':sout-keep'
