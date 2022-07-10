package com.bristle.apigateway.service;

import com.bristle.apigateway.converter.production_ticket.ProductionTicketEntityConverter;
import com.bristle.apigateway.model.production_ticket.ProductionTicketEntity;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.production_ticket.DeleteProductionTicketRequest;
import com.bristle.proto.production_ticket.DeleteProductionTicketResponse;
import com.bristle.proto.production_ticket.GetProductionTicketsRequest;
import com.bristle.proto.production_ticket.GetProductionTicketsResponse;
import com.bristle.proto.production_ticket.ProductionTicketFilter;
import com.bristle.proto.production_ticket.ProductionTicketServiceGrpc;
import com.bristle.proto.production_ticket.UpsertProductionTicketRequest;
import com.bristle.proto.production_ticket.UpsertProductionTicketResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductionTicketService {

    Logger log = LoggerFactory.getLogger(ProductionTicketService.class);

    @GrpcClient("production_ticket_grpc_service")
    ProductionTicketServiceGrpc.ProductionTicketServiceBlockingStub m_ProductionTicketGrpcService;
    private final ProductionTicketEntityConverter m_productionTicketConverter;

    ProductionTicketService(ProductionTicketEntityConverter productionTicketConverter) {
        this.m_productionTicketConverter = productionTicketConverter;
    }

    public ProductionTicketEntity upsertProductionTicket(RequestContext.Builder requestContext,
                                                         ProductionTicketEntity ticketEntity) throws Exception {
        // if this is the first time issuing this order
        // put current time as issued_at
        if (ticketEntity.getIssuedAt() == null) {
            ticketEntity.setIssuedAt(LocalDateTime.now());
        }

        UpsertProductionTicketRequest request = UpsertProductionTicketRequest.newBuilder()
                .setRequestContext(requestContext)
                .setProductionTicket(m_productionTicketConverter.entityToProto(ticketEntity)).build();
        UpsertProductionTicketResponse response =
                m_ProductionTicketGrpcService.upsertProductionTicket(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_productionTicketConverter.protoToEntity(response.getProductionTicket());
    }

    public ProductionTicketEntity deleteProductionTicket(RequestContext.Builder requestContext,
                                                         Integer ticketId) throws Exception {
        DeleteProductionTicketRequest request = DeleteProductionTicketRequest.newBuilder()
                .setRequestContext(requestContext)
                .setProductionTicketId(ticketId).build();
        DeleteProductionTicketResponse response =
                m_ProductionTicketGrpcService.deleteProductionTicket(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_productionTicketConverter.protoToEntity(response.getProductionTicket());
    }

    public List<ProductionTicketEntity> getProductionTicket(RequestContext.Builder requestContext,
                                                            Integer pageIndex,
                                                            Integer pageSize,
                                                            Integer ticketId,
                                                            String customerId,
                                                            String bristleType,
                                                            String model,
                                                            String productName,
                                                            Date dueDateFrom,
                                                            Date dueDateTo,
                                                            LocalDateTime issuedAtFrom,
                                                            LocalDateTime issuedAtTo) throws Exception {

        ProductionTicketFilter.Builder filter = ProductionTicketFilter.newBuilder();

        filter.setTicketId(ticketId == null ? Integer.MIN_VALUE : ticketId);
        filter.setCustomerId(customerId == null ? "" : customerId);
        filter.setBristleType(bristleType == null ? "" : bristleType);
        filter.setModel(model == null ? "" : model);
        filter.setProductName(productName == null ? "" : productName);
        filter.setDueDateFrom(dueDateFrom == null ? Long.MIN_VALUE : dueDateFrom.getTime());
        filter.setDueDateTo(dueDateFrom == null ? Long.MIN_VALUE : dueDateTo.getTime());
        filter.setIssuedAtFrom(issuedAtFrom == null ? Long.MIN_VALUE : issuedAtFrom.toEpochSecond(ZoneOffset.UTC));
        filter.setIssuedAtTo(issuedAtTo == null ? Long.MIN_VALUE : issuedAtTo.toEpochSecond(ZoneOffset.UTC));

        GetProductionTicketsRequest request = GetProductionTicketsRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(filter)
                .setPageIndex(pageIndex == null ? 0 : pageIndex)
                .setPageSize(pageSize == null ? 20 : pageSize).build();
        GetProductionTicketsResponse response = m_ProductionTicketGrpcService.getProductionTickets(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return response.getProductionTicketList().stream()
                .map(m_productionTicketConverter::protoToEntity).collect(Collectors.toList());
    }
}
