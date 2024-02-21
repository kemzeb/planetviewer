# This script fires up the production environment.

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")
CURRENT_DIR=$(pwd)

if [ "$CURRENT_DIR" != "$PROJECT_ROOT" ]; then
    echo "Please run this script from the project's root directory."
    exit 1
fi

if [  ! -f .env ]; then
    echo "Creating example .env file."
    sh meta/create-example-env.sh
fi

sh meta/generate-es-certs.sh && docker-compose up
