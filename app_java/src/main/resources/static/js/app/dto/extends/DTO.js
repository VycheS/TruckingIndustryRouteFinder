class DTO {
    getAllStrJson(valuesMap) {
        str = "{";
        valuesMap.forEach((key, value) => {
            str += "\"" + key + "\":\"" + value + "\",";
        });
        str += "}";
        return str;
    }
}