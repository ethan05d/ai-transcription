# Use the official node image as base
FROM node:18-bullseye-slim

# Set the working directory
WORKDIR /app

# Copy only package.json and package-lock.json to install dependencies first (improves caching)
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Expose port 3000 for the Vite development server
EXPOSE 3000

# Run Vite in development mode
CMD ["npm", "run", "dev"]
