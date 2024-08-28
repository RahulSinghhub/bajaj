package com.md5.Hash;
import java.io.File;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class HashApplication {

	public static void main(String[] args) {
		  if (args.length != 2) {
	            System.out.println("Usage: java -jar HashApplication.jar <240341220148> <D:\\java\\exam\\example.json>");
	            return;
	        }

	        String prnNumber = args[0].toLowerCase();
	        String jsonFilePath = args[1];

	        try {
	            // Parse the JSON file using Jackson
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode jsonNode = objectMapper.readTree(new File(jsonFilePath));

	            // Find the destination value
	            String destinationValue = findDestination(jsonNode);

	            if (destinationValue == null) {
	                System.out.println("Key 'destination' not found in JSON file.");
	                return;
	            }

	            String randomString = generateRandomString(8);
	            String concatenatedValue = prnNumber + destinationValue + randomString;
	            String md5Hash = generateMD5Hash(concatenatedValue);
	            String result = md5Hash + ";" + randomString;

	            System.out.println(result);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static String findDestination(JsonNode jsonNode) {
	        if (jsonNode.has("destination")) {
	            return jsonNode.get("destination").asText();
	        }

	        for (JsonNode childNode : jsonNode) {
	            if (childNode.isObject()) {
	                String result = findDestination(childNode);
	                if (result != null) return result;
	            }
	        }
	        return null;
	    }

	    private static String generateRandomString(int length) {
	        // Random string generation logic
	    	String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        StringBuilder sb = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int index = (int) (Math.random() * chars.length());
	            sb.append(chars.charAt(index));
	        }
	        return sb.toString();
	    }

	    private static String generateMD5Hash(String input) {
	        // MD5 hash generation logic
	    	try {
	            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	            byte[] array = md.digest(input.getBytes());
	            StringBuilder sb = new StringBuilder();
	            for (byte b : array) {
	                sb.append(String.format("%02x", b));
	            }
	            return sb.toString();
	        } catch (java.security.NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	}