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


export PATH=$PATH:../../../src/components/cpp/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:../../../src/api/cpp:../../../src/keyvalue_parser:$ZMQ_LIB_DIR:$PROTOBUF_LIB_DIR

 control_c()
# run if user hits control-c
{
  kill -9 $SDPID
  kill -9 $PUBPID
  exit $?
}

# trap keyboard interrupt (control-c)
trap control_c SIGINT

ServiceDirectory &
SDPID=$!
./BasicDataProductPublisher &
PUBPID=$!

# sleep for a second to give the publisher a chance to register its data product
sleep 1

./BasicDataProductSubscriber