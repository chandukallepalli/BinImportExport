package com.example.poc.util;



public class CommonFile {


    public static String frameNumberToTime(int frameNumber) {
        int totalSeconds = frameNumber - 1;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String toBinaryStringWithLeadingZeros(int value, int bits) {
        return String.format("%" + bits + "s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public static String bytesToHex(byte[] bytes, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < offset + length; i++) {
            if (i < bytes.length) {
                sb.append(String.format("%02X", bytes[i]));
            } else {
                sb.append("00");
            }
        }
        return sb.toString();
    }

    public static int byteArrayToInt(byte[] bytes, int offset, int length) {
        int result = 0;

        for (int i = 0; i < length; i++) {
            result = (result << 8) + (bytes[offset + i] & 0xFF);
        }
        return result;
    }

    public static int extractBits(byte[] data, int startBit, int numberOfBits) {
        int byteIndex = startBit / 8;
        int bitIndex = startBit % 8;
        int value = 0;

        for (int i = 0; i < numberOfBits; i++) {
            int bit = (data[byteIndex] >> (7 - bitIndex)) & 1;
            value = (value << 1) | bit;

            bitIndex++;
            if (bitIndex == 8) {
                bitIndex = 0;
                byteIndex++;
            }
        }

        return value;
    }

    public static String decodeDate(byte[] data, int offset) {
        int day = data[offset] & 0xFF;
        int month = data[offset + 1] & 0xFF;
        int year = data[offset + 2] & 0xFF;
        return String.format("%02d/%02d/%02d", day, month, year);
    }

    public static String decodeTime(byte[] data, int offset) {
        int hours = data[offset] & 0xFF;
        int minutes = data[offset + 1] & 0xFF;
        int seconds = data[offset + 2] & 0xFF;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String decodeStationActiveRadio(byte[] data, int offset, int length) {
        if (data.length < offset + length) {
            return "Data too short to decode.";
        }

        int value = data[offset] & 0xFF;

        String hexValue = String.format("%02X", value);

        switch (value) {
            case 0xF1:
                return "Radio 1 is active.";
            case 0xF2:
                return "Radio 2 is active.";
            case 0xE1:
                return "Ethernet 1 is active.";
            case 0xE2:
                return "Ethernet 2 is active.";
            default:
                return "Active radio unknown. Hex Value: ";
        }
    }

    public static int findNextSOF(byte[] data, int start) {
        for (int i = start; i <= data.length - 2; i++) {
            if ("AAAA".equals(bytesToHex(data, i, 2))) {
          
                return i;
            }
        }
        return data.length; // No more SOF found, return the end of the data
    }


    public static String extractBitsAsString(byte[] data, int startBit, int numberOfBits) {
        int byteIndex = startBit / 8;
        int bitIndex = startBit % 8;
        StringBuilder bitString = new StringBuilder();

        for (int i = 0; i < numberOfBits; i++) {
            int bit = (data[byteIndex] >> (7 - bitIndex)) & 1;
            bitString.append(bit);

            bitIndex++;
            if (bitIndex == 8) {
                bitIndex = 0;
                byteIndex++;
            }
        }

        return bitString.toString();
    }


}

