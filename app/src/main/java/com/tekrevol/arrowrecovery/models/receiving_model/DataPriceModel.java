package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

import java.util.List;

public class DataPriceModel {

    @Expose
    @SerializedName("dataset_data")
    private DatasetDataEntity datasetData;

    public DatasetDataEntity getDatasetData() {
        return datasetData;
    }

    public void setDatasetData(DatasetDataEntity datasetData) {
        this.datasetData = datasetData;
    }

    public static class DatasetDataEntity {
        @Expose
        @SerializedName("order")
        private String order;
        @Expose
        @SerializedName("collapse")
        private String collapse;
        @Expose
        @SerializedName("data")
        private List<String> data;
        @Expose
        @SerializedName("frequency")
        private String frequency;
        @Expose
        @SerializedName("end_date")
        private String endDate;
        @Expose
        @SerializedName("start_date")
        private String startDate;
        @Expose
        @SerializedName("column_names")
        private List<String> columnNames;
        @Expose
        @SerializedName("column_index")
        private String columnIndex;
        @Expose
        @SerializedName("transform")
        private String transform;
        @Expose
        @SerializedName("limit")
        private String limit;

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getCollapse() {
            return collapse;
        }

        public void setCollapse(String collapse) {
            this.collapse = collapse;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public List<String> getColumnNames() {
            return columnNames;
        }

        public void setColumnNames(List<String> columnNames) {
            this.columnNames = columnNames;
        }

        public String getColumnIndex() {
            return columnIndex;
        }

        public void setColumnIndex(String columnIndex) {
            this.columnIndex = columnIndex;
        }

        public String getTransform() {
            return transform;
        }

        public void setTransform(String transform) {
            this.transform = transform;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
