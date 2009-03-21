@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM CMPP3.0 Gateway Client Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM CMPPGW_HOME - location of cmppgw's installed home dir
@REM CMPPGW_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM CMPPGW_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM CMPPGW_OPTS - parameters passed to the Java VM when running cmppgw
@REM     e.g. to debug cmppgw itself, use
@REM set CMPPGW_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case CMPPGW_BATCH_ECHO is 'on'
@echo off
@REM enable echoing my setting CMPPGW_BATCH_ECHO to 'on'
@if "%CMPPGW_BATCH_ECHO%" == "on"  echo %CMPPGW_BATCH_ECHO%

@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set HOME=%HOMEDRIVE%%HOMEPATH%)

@REM Execute a user defined script before this one
if exist "%HOME%\cmppgwrc_pre.bat" call "%HOME%\cmppgwrc_pre.bat"

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkCmppgwHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:chkCmppgwHome
if not "%CMPPGW_HOME%"=="" goto valCmppgwHome

if "%OS%"=="Windows_NT" SET CMPPGW_HOME=%~dp0..
if "%OS%"=="WINNT" SET CMPPGW_HOME=%~dp0..
if not "%CMPPGW_HOME%"=="" goto valCmppgwHome

echo.
echo ERROR: CMPPGW_HOME not found in your environment.
echo Please set the CMPPGW_HOME variable in your environment to match the
echo location of the cmppgw installation
echo.
goto error

:valCmppgwHome

:stripCmppgwHome
if not _%CMPPGW_HOME:~-1%==_\ goto checkCmppgwBat
set CMPPGW_HOME=%CMPPGW_HOME:~0,-1%
goto stripCmppgwHome

:checkCmppgwBat
if exist "%CMPPGW_HOME%\bin\cmppgw.bat" goto init

echo.
echo ERROR: CMPPGW_HOME is set to an invalid directory.
echo CMPPGW_HOME = %CMPPGW_HOME%
echo Please set the CMPPGW_HOME variable in your environment to match the
echo location of the cmppgw installation
echo.
goto error
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Windows NT with Novell Login
if "%OS%"=="WINNT" goto WinNTNovell

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

:WinNTNovell

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMPPGW_CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set CMPPGW_CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set CMPPGW_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set CMPPGW_CMD_LINE_ARGS=%CMPPGW_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
SET CMPPGW_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

set CMPPGW_CLASSPATH=..\conf

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTCWJars

@REM -- Regular WinNT shell
for /F %%i in ('dir "%CMPPGW_HOME%\lib\*.jar" /b') do call :Loop "%CMPPGW_HOME%\lib\%%i"
goto runm2

@REM The 4NT Shell from jp software
:4NTCWJars
for /F %%i in ('dir "%CMPPGW_HOME%\lib\*.jar" /b') do call :Loop "%CMPPGW_HOME%\lib\%%i"
goto runm2

:Loop
set CMPPGW_CLASSPATH=%CMPPGW_CLASSPATH%;%1
goto end

@REM Start CMPPGW
:runm2
%CMPPGW_JAVA_EXE% %CMPPGW_OPTS% -classpath %CMPPGW_CLASSPATH% -Dcmppgw.home="%CMPPGW_HOME%" com.enorbus.sms.gw.cmpp.console.Main %CMPPGW_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT
if "%OS%"=="WINNT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMPPGW_JAVA_EXE=
set CMPPGW_CMD_LINE_ARGS=
set CMPPGW_CLASSPATH=
goto postExec

:endNT
@endlocal & set ERROR_CODE=%ERROR_CODE%

:postExec
if exist "%HOME%\cmppgwrc_post.bat" call "%HOME%\cmppgwrc_post.bat"
@REM pause the batch file if CMPPGW_BATCH_PAUSE is set to 'on'
if "%CMPPGW_BATCH_PAUSE%" == "on" pause

if "%CMPPGW_TERMINATE_CMD%" == "on" exit %ERROR_CODE%

exit /B %ERROR_CODE%

