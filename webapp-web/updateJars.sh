#
#
# Copyright (C) 2002-2012 "SYSNET International, Inc."
# support@sysnetint.com [http://www.sysnetint.com]
#
# This file is part of OpenEMPI.
#
# OpenEMPI is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#

cp ../core/target/openempi-core-2.1.3-SNAPSHOT.jar target/openempi-webapp-web-2.1.3-SNAPSHOT/WEB-INF/lib/openempi-core-2.1.3-SNAPSHOT.jar
cp ../core/target/openempi-core-2.1.3-SNAPSHOT.jar war/WEB-INF/lib/openempi-core-2.1.3-SNAPSHOT.jar
cp ../webapp-server/target/openempi-webapp-server-2.1.3-SNAPSHOT.jar target/openempi-webapp-web-2.1.3-SNAPSHOT/WEB-INF/lib/openempi-webapp-server-2.1.3-SNAPSHOT.jar
cp ../webapp-server/target/openempi-webapp-server-2.1.3-SNAPSHOT.jar war/WEB-INF/lib/openempi-webapp-server-2.1.3-SNAPSHOT.jar
cp ../pixpdq-adapter/target/openempi-pixpdq-adapter-2.1.3-SNAPSHOT.jar target/openempi-webapp-web-2.1.3-SNAPSHOT/WEB-INF/lib
cp ../pixpdq-adapter/target/openempi-pixpdq-adapter-2.1.3-SNAPSHOT.jar war/WEB-INF/lib/openempi-webapp-server-2.1.3-SNAPSHOT.jar
