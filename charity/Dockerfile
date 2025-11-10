# Étape 1 : Build Angular
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# ⚡ Build Angular (en mode production)
RUN npm run build -- --configuration production --project charity --no-progress --verbose --delete-output-path=false --optimization=false

# Étape 2 : Serve avec Nginx
FROM nginx:stable-alpine
COPY --from=build /app/dist/charity/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
