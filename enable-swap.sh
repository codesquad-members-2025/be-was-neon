#!/usr/bin/env bash
set -e

# 스왑 사이즈 (단위: MB)
SWAP_SIZE=2048  # 2GB

echo "📦 Creating ${SWAP_SIZE}MB swap file at /swapfile..."

# 1. 스왑 파일 생성
sudo fallocate -l "${SWAP_SIZE}M" /swapfile

# 2. 권한 설정
sudo chmod 600 /swapfile

# 3. 스왑 영역으로 설정
sudo mkswap /swapfile

# 4. 스왑 활성화
sudo swapon /swapfile

# 5. 재부팅 시 자동 적용 설정 (fstab에 추가)
if ! grep -q '/swapfile' /etc/fstab; then
  echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
fi

# 6. 확인
echo "✅ Swap enabled:"
swapon --show
