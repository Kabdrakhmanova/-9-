import java.util.HashMap;
import java.util.Map;

interface IInternalDeliveryService {
    void deliverOrder(String orderId);
    String getDeliveryStatus(String orderId);
    double calculateDeliveryCost(double weight, double distance);
}

class InternalDeliveryService implements IInternalDeliveryService {
    private Map<String, String> orders = new HashMap<>();

    public void deliverOrder(String orderId) {
        orders.put(orderId, "Доставка в процессе");
        System.out.println("Внутренняя служба: заказ " + orderId + " доставляется");
    }

    public String getDeliveryStatus(String orderId) {
        return orders.getOrDefault(orderId, "Неизвестный заказ");
    }

    public double calculateDeliveryCost(double weight, double distance) {
        return weight * distance * 0.5;
    }
}

class ExternalLogisticsServiceA {
    public void shipItem(int itemId) {
        System.out.println("Сторонняя служба A: отправка товара с ID " + itemId);
    }

    public String trackShipment(int shipmentId) {
        return "Статус доставки товара с ID " + shipmentId + " от службы A: Доставка в процессе";
    }
}

class LogisticsAdapterA implements IInternalDeliveryService {
    private ExternalLogisticsServiceA externalService;

    public LogisticsAdapterA(ExternalLogisticsServiceA externalService) {
        this.externalService = externalService;
    }

    public void deliverOrder(String orderId) {
        try {
            int itemId = Integer.parseInt(orderId);
            externalService.shipItem(itemId);
        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования ID заказа для службы A: " + e.getMessage());
        }
    }

    public String getDeliveryStatus(String orderId) {
        try {
            int shipmentId = Integer.parseInt(orderId);
            return externalService.trackShipment(shipmentId);
        } catch (NumberFormatException e) {
            return "Ошибка получения статуса для службы A: некорректный ID заказа";
        }
    }

    public double calculateDeliveryCost(double weight, double distance) {
        return weight * distance * 0.7;
    }
}

class ExternalLogisticsServiceB {
    public void sendPackage(String packageInfo) {
        System.out.println("Сторонняя служба B: отправка посылки " + packageInfo);
    }

    public String checkPackageStatus(String trackingCode) {
        return "Статус посылки с трекинг-кодом " + trackingCode + " от службы B: В пути";
    }
}

class LogisticsAdapterB implements IInternalDeliveryService {
    private ExternalLogisticsServiceB externalService;

    public LogisticsAdapterB(ExternalLogisticsServiceB externalService) {
        this.externalService = externalService;
    }

    public void deliverOrder(String orderId) {
        externalService.sendPackage("Посылка ID: " + orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return externalService.checkPackageStatus(orderId);
    }

    public double calculateDeliveryCost(double weight, double distance) {
        return weight * distance * 0.6;
    }
}

class ExternalLogisticsServiceC {
    public void dispatch(String cargoInfo) {
        System.out.println("Сторонняя служба C: отправка груза " + cargoInfo);
    }

    public String cargoStatus(String cargoId) {
        return "Статус груза с ID " + cargoId + " от службы C: Отправлен";
    }
}

class LogisticsAdapterC implements IInternalDeliveryService {
    private ExternalLogisticsServiceC externalService;

    public LogisticsAdapterC(ExternalLogisticsServiceC externalService) {
        this.externalService = externalService;
    }

    public void deliverOrder(String orderId) {
        externalService.dispatch("Груз ID: " + orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return externalService.cargoStatus(orderId);
    }

    public double calculateDeliveryCost(double weight, double distance) {
        return weight * distance * 0.8;
    }
}

class DeliveryServiceFactory {
    public static IInternalDeliveryService getDeliveryService(String serviceType) {
        switch (serviceType) {
            case "internal":
                return new InternalDeliveryService();
            case "externalA":
                return new LogisticsAdapterA(new ExternalLogisticsServiceA());
            case "externalB":
                return new LogisticsAdapterB(new ExternalLogisticsServiceB());
            case "externalC":
                return new LogisticsAdapterC(new ExternalLogisticsServiceC());
            default:
                throw new IllegalArgumentException("Неизвестный тип службы доставки: " + serviceType);
        }
    }
}

public class LogisticsSystem {
    public static void main(String[] args) {
        IInternalDeliveryService internalService = DeliveryServiceFactory.getDeliveryService("internal");
        internalService.deliverOrder("123");
        System.out.println(internalService.getDeliveryStatus("123"));
        System.out.println("Стоимость доставки: " + internalService.calculateDeliveryCost(10, 50));

        IInternalDeliveryService externalServiceA = DeliveryServiceFactory.getDeliveryService("externalA");
        externalServiceA.deliverOrder("456");
        System.out.println(externalServiceA.getDeliveryStatus("456"));
        System.out.println("Стоимость доставки: " + externalServiceA.calculateDeliveryCost(10, 50));

        IInternalDeliveryService externalServiceB = DeliveryServiceFactory.getDeliveryService("externalB");
        externalServiceB.deliverOrder("789");
        System.out.println(externalServiceB.getDeliveryStatus("789"));
        System.out.println("Стоимость доставки: " + externalServiceB.calculateDeliveryCost(10, 50));

        IInternalDeliveryService externalServiceC = DeliveryServiceFactory.getDeliveryService("externalC");
        externalServiceC.deliverOrder("1011");
        System.out.println(externalServiceC.getDeliveryStatus("1011"));
        System.out.println("Стоимость доставки: " + externalServiceC.calculateDeliveryCost(10, 50));
    }
}
