# Multi-stage Dockerfile for an Angular frontend
# Place this Dockerfile in the Angular project root (the frontend folder)

# 1) Build stage
FROM node:18-alpine AS build
WORKDIR /app

# Optionally set base href at build time: docker build --build-arg APP_BASE=/myapp/ .
ARG APP_BASE=/
ENV NG_CLI_ANALYTICS=false

# Install deps
COPY package*.json ./
RUN npm ci --silent

# Copy source and build
COPY . .
# Build production bundle (Angular CLI)
RUN npm run build -- --configuration=production --output-path=./dist --base-href=${APP_BASE}

# 2) Production image served by nginx
FROM nginx:stable-alpine AS production
ENV PORT=80
EXPOSE ${PORT}

# Remove default config and write a simple SPA-friendly config
RUN rm /etc/nginx/conf.d/default.conf \
 && printf 'server {\n  listen 80;\n  server_name _;\n  root /usr/share/nginx/html;\n  index index.html;\n  location / {\n    try_files $uri $uri/ /index.html;\n  }\n  gzip on;\n  gzip_types text/plain text/css application/javascript application/json image/svg+xml;\n  add_header Cache-Control "public, max-age=31536000, immutable" always;\n}\n' > /etc/nginx/conf.d/default.conf

# Copy built assets
COPY --from=build /app/dist/ /usr/share/nginx/html/

# Keep nginx in foreground
CMD ["nginx", "-g", "daemon off;"]