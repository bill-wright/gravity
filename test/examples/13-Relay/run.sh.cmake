#!/bin/bash
#** (C) Copyright 2013, Applied Physical Sciences Corp., A General Dynamics Company
#**
#** Gravity is free software; you can redistribute it and/or modify
#** it under the terms of the GNU Lesser General Public License as published by
#** the Free Software Foundation; either version 3 of the License, or
#** (at your option) any later version.
#**
#** This program is distributed in the hope that it will be useful,
#** but WITHOUT ANY WARRANTY; without even the implied warranty of
#** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#** GNU Lesser General Public License for more details.
#**
#** You should have received a copy of the GNU Lesser General Public
#** License along with this program;
#** If not, see <http://www.gnu.org/licenses/>.
#**

# Tool-related substitution variables
PROTOBUF_LIB_DIR=${PROTOBUF_LIBRARY_PATH}
ZMQ_LIB_DIR=${ZeroMQ_LIBRARY_PATH}

export PATH=$PATH:${ServiceDirectory_BINARY_DIR}:${Relay_BINARY_DIR}
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${libgravity_BINARY_DIR}:${libkeyvalue_parser_BINARY_DIR}:$ZMQ_LIB_DIR:$PROTOBUF_LIB_DIR


cleanup()
# run if user hits control-c
{
  kill $SDPID
  if [ ! -z "${RPID}" ]; then
    kill -9 $RPID > /dev/null 2>&1 
  fi
  kill $PUBPID
  kill $SUBPID
  exit $?
}

# trap keyboard interrupt (control-c)
trap cleanup SIGINT

ServiceDirectory &
SDPID=$!

# sleep for a second to give the service directory a chance to start up
sleep 1

./ProtobufDataProductPublisher &
PUBPID=$!

./ProtobufDataProductSubscriber &
SUBPID=$!

sleep 3

Relay &
RPID=$!

sleep 10

echo "Killing Relay"
kill -9 $RPID

sleep 10

cleanup
