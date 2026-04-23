FROM node:16-alpine
WORKDIR /app
COPY . .
RUN npm install
RUN npx react-scripts build
ENV NODE_ENV=production
EXPOSE 3000
CMD ["npx", "serve", "-s", "build"]
