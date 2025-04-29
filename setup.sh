#!/bin/bash

set -e  # 에러 발생 시 스크립트 중지

echo "[1] 패키지 목록 업데이트"
sudo apt update

echo "[3] Java 설치 (필요한 경우)"
sudo apt install -y openjdk-21-jdk

echo "[5] 추가 필요한 패키지 설치 (예: 빌드 툴)"
# 예시로 gradle 설치
sudo apt install -y gradle

echo "[6] Setup 완료"
