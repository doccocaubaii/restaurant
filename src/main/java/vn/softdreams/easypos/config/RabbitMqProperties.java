package vn.softdreams.easypos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq", ignoreUnknownFields = false)
public class RabbitMqProperties {

    private String host;
    private String username;
    private String password;
    private String virtualHost;

    private EasyInvoice easyInvoice;
    private NgoGiaPhatInvoice ngoGiaPhatInvoice;
    private EasyBooks88 easyBooks88;
    private Consumer consumer;
    private Producer producer;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public NgoGiaPhatInvoice getNgoGiaPhatInvoice() {
        return ngoGiaPhatInvoice;
    }

    public void setNgoGiaPhatInvoice(NgoGiaPhatInvoice ngoGiaPhatInvoice) {
        this.ngoGiaPhatInvoice = ngoGiaPhatInvoice;
    }

    public EasyInvoice getEasyInvoice() {
        return easyInvoice;
    }

    public void setEasyInvoice(EasyInvoice easyInvoice) {
        this.easyInvoice = easyInvoice;
    }

    public EasyBooks88 getEasyBooks88() {
        return easyBooks88;
    }

    public void setEasyBooks88(EasyBooks88 easyBooks88) {
        this.easyBooks88 = easyBooks88;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public static class NgoGiaPhatInvoice {

        private String ngpCheckInvoiceQueue;
        private String ngpCheckInvoiceRoutingKey;
        private String ngpImportInvoiceQueue;
        private String ngpImportInvoiceRoutingKey;
        private String ngpIssueInvoiceQueue;
        private String ngpIssueInvoiceRoutingKey;
        private String ngpReplaceInvoiceQueue;
        private String ngpReplaceInvoiceRoutingKey;
        private String ngpCancelInvoiceQueue;
        private String ngpCancelInvoiceRoutingKey;

        public String getNgpIssueInvoiceQueue() {
            return ngpIssueInvoiceQueue;
        }

        public void setNgpIssueInvoiceQueue(String ngpIssueInvoiceQueue) {
            this.ngpIssueInvoiceQueue = ngpIssueInvoiceQueue;
        }

        public String getNgpIssueInvoiceRoutingKey() {
            return ngpIssueInvoiceRoutingKey;
        }

        public void setNgpIssueInvoiceRoutingKey(String ngpIssueInvoiceRoutingKey) {
            this.ngpIssueInvoiceRoutingKey = ngpIssueInvoiceRoutingKey;
        }

        public String getNgpCheckInvoiceQueue() {
            return ngpCheckInvoiceQueue;
        }

        public void setNgpCheckInvoiceQueue(String ngpCheckInvoiceQueue) {
            this.ngpCheckInvoiceQueue = ngpCheckInvoiceQueue;
        }

        public String getNgpCheckInvoiceRoutingKey() {
            return ngpCheckInvoiceRoutingKey;
        }

        public void setNgpCheckInvoiceRoutingKey(String ngpCheckInvoiceRoutingKey) {
            this.ngpCheckInvoiceRoutingKey = ngpCheckInvoiceRoutingKey;
        }

        public String getNgpReplaceInvoiceQueue() {
            return ngpReplaceInvoiceQueue;
        }

        public void setNgpReplaceInvoiceQueue(String ngpReplaceInvoiceQueue) {
            this.ngpReplaceInvoiceQueue = ngpReplaceInvoiceQueue;
        }

        public String getNgpReplaceInvoiceRoutingKey() {
            return ngpReplaceInvoiceRoutingKey;
        }

        public void setNgpReplaceInvoiceRoutingKey(String ngpReplaceInvoiceRoutingKey) {
            this.ngpReplaceInvoiceRoutingKey = ngpReplaceInvoiceRoutingKey;
        }

        public String getNgpImportInvoiceQueue() {
            return ngpImportInvoiceQueue;
        }

        public void setNgpImportInvoiceQueue(String ngpImportInvoiceQueue) {
            this.ngpImportInvoiceQueue = ngpImportInvoiceQueue;
        }

        public String getNgpImportInvoiceRoutingKey() {
            return ngpImportInvoiceRoutingKey;
        }

        public void setNgpImportInvoiceRoutingKey(String ngpImportInvoiceRoutingKey) {
            this.ngpImportInvoiceRoutingKey = ngpImportInvoiceRoutingKey;
        }

        public String getNgpCancelInvoiceQueue() {
            return ngpCancelInvoiceQueue;
        }

        public void setNgpCancelInvoiceQueue(String ngpCancelInvoiceQueue) {
            this.ngpCancelInvoiceQueue = ngpCancelInvoiceQueue;
        }

        public String getNgpCancelInvoiceRoutingKey() {
            return ngpCancelInvoiceRoutingKey;
        }

        public void setNgpCancelInvoiceRoutingKey(String ngpCancelInvoiceRoutingKey) {
            this.ngpCancelInvoiceRoutingKey = ngpCancelInvoiceRoutingKey;
        }
    }

    public static class EasyInvoice {

        private String issueInvoiceQueue;
        private String issueInvoiceRoutingKey;
        private String checkInvoiceQueue;
        private String checkInvoiceRoutingKey;
        private String replaceInvoiceQueue;
        private String replaceInvoiceRoutingKey;
        private String importInvoiceQueue;
        private String importInvoiceRoutingKey;
        private String cancelInvoiceQueue;
        private String cancelInvoiceRoutingKey;

        public String getIssueInvoiceQueue() {
            return issueInvoiceQueue;
        }

        public void setIssueInvoiceQueue(String issueInvoiceQueue) {
            this.issueInvoiceQueue = issueInvoiceQueue;
        }

        public String getIssueInvoiceRoutingKey() {
            return issueInvoiceRoutingKey;
        }

        public void setIssueInvoiceRoutingKey(String issueInvoiceRoutingKey) {
            this.issueInvoiceRoutingKey = issueInvoiceRoutingKey;
        }

        public String getCheckInvoiceQueue() {
            return checkInvoiceQueue;
        }

        public void setCheckInvoiceQueue(String checkInvoiceQueue) {
            this.checkInvoiceQueue = checkInvoiceQueue;
        }

        public String getCheckInvoiceRoutingKey() {
            return checkInvoiceRoutingKey;
        }

        public void setCheckInvoiceRoutingKey(String checkInvoiceRoutingKey) {
            this.checkInvoiceRoutingKey = checkInvoiceRoutingKey;
        }

        public String getReplaceInvoiceQueue() {
            return replaceInvoiceQueue;
        }

        public void setReplaceInvoiceQueue(String replaceInvoiceQueue) {
            this.replaceInvoiceQueue = replaceInvoiceQueue;
        }

        public String getReplaceInvoiceRoutingKey() {
            return replaceInvoiceRoutingKey;
        }

        public void setReplaceInvoiceRoutingKey(String replaceInvoiceRoutingKey) {
            this.replaceInvoiceRoutingKey = replaceInvoiceRoutingKey;
        }

        public String getImportInvoiceQueue() {
            return importInvoiceQueue;
        }

        public void setImportInvoiceQueue(String importInvoiceQueue) {
            this.importInvoiceQueue = importInvoiceQueue;
        }

        public String getImportInvoiceRoutingKey() {
            return importInvoiceRoutingKey;
        }

        public void setImportInvoiceRoutingKey(String importInvoiceRoutingKey) {
            this.importInvoiceRoutingKey = importInvoiceRoutingKey;
        }

        public String getCancelInvoiceQueue() {
            return cancelInvoiceQueue;
        }

        public void setCancelInvoiceQueue(String cancelInvoiceQueue) {
            this.cancelInvoiceQueue = cancelInvoiceQueue;
        }

        public String getCancelInvoiceRoutingKey() {
            return cancelInvoiceRoutingKey;
        }

        public void setCancelInvoiceRoutingKey(String cancelInvoiceRoutingKey) {
            this.cancelInvoiceRoutingKey = cancelInvoiceRoutingKey;
        }
    }

    public static class EasyBooks88 {

        private String sendQueue;
        private String sendRoutingKey;

        public String getSendQueue() {
            return sendQueue;
        }

        public void setSendQueue(String sendQueue) {
            this.sendQueue = sendQueue;
        }

        public String getSendRoutingKey() {
            return sendRoutingKey;
        }

        public void setSendRoutingKey(String sendRoutingKey) {
            this.sendRoutingKey = sendRoutingKey;
        }
    }

    public static class Consumer {

        private Integer maxConcurrentConsumer;
        private Integer concurrentConsumer;
        private Integer prefetchCount;

        public Integer getMaxConcurrentConsumer() {
            return maxConcurrentConsumer;
        }

        public void setMaxConcurrentConsumer(Integer maxConcurrentConsumer) {
            this.maxConcurrentConsumer = maxConcurrentConsumer;
        }

        public Integer getConcurrentConsumer() {
            return concurrentConsumer;
        }

        public void setConcurrentConsumer(Integer concurrentConsumer) {
            this.concurrentConsumer = concurrentConsumer;
        }

        public Integer getPrefetchCount() {
            return prefetchCount;
        }

        public void setPrefetchCount(Integer prefetchCount) {
            this.prefetchCount = prefetchCount;
        }
    }

    public static class Producer {

        private Integer replyTimeout;
        private String directExchange;

        public Integer getReplyTimeout() {
            return replyTimeout;
        }

        public void setReplyTimeout(Integer replyTimeout) {
            this.replyTimeout = replyTimeout;
        }

        public String getDirectExchange() {
            return directExchange;
        }

        public void setDirectExchange(String directExchange) {
            this.directExchange = directExchange;
        }
    }
}
