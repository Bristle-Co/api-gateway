package com.bristle.apigateway.service.production_ticket;

import com.bristle.apigateway.converter.production_ticket.ProductionTicketEntityConverter;
import com.bristle.apigateway.model.dto.production_ticket.ProductionTicketDto;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.common.ResponseContext;
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
import java.util.List;
import java.util.Optional;
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

    public ProductionTicketDto upsertProductionTicket(RequestContext.Builder requestContext,
                                                      ProductionTicketDto ticketDto) throws Exception {
        // if this is the first time issuing this order
        // put current time as issued_at
        if (ticketDto.getIssuedAt() == null) {
            ticketDto.setIssuedAt(LocalDateTime.now());
        }

        UpsertProductionTicketRequest request = UpsertProductionTicketRequest.newBuilder()
                .setRequestContext(requestContext)
                .setProductionTicket(m_productionTicketConverter.dtoToProto(ticketDto)).build();
        UpsertProductionTicketResponse response =
                m_ProductionTicketGrpcService.upsertProductionTicket(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_productionTicketConverter.protoToDto(response.getProductionTicket());
    }

    public ProductionTicketDto deleteProductionTicket(RequestContext.Builder requestContext,
                                                         Integer ticketId) throws Exception {
        DeleteProductionTicketRequest request = DeleteProductionTicketRequest.newBuilder()
                .setRequestContext(requestContext)
                .setProductionTicketId(ticketId).build();
        DeleteProductionTicketResponse response =
                m_ProductionTicketGrpcService.deleteProductionTicket(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_productionTicketConverter.protoToDto(response.getProductionTicket());
    }

    public List<ProductionTicketDto> getProductionTicket(RequestContext.Builder requestContext,
                                                     Integer pageIndex,
                                                     Integer pageSize,
                                                     ProductionTicketFilter filter) throws Exception {


        GetProductionTicketsRequest request = GetProductionTicketsRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(filter)
                .setPageIndex(pageIndex)
                .setPageSize(pageSize).build();
        GetProductionTicketsResponse response = m_ProductionTicketGrpcService.getProductionTickets(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return response.getProductionTicketList().stream()
                .map(m_productionTicketConverter::protoToDto).collect(Collectors.toList());
    }

    public Optional<ProductionTicketDto> getProductionTicketById(RequestContext.Builder requestContext,
                                                         Integer ticketId) throws Exception {


        GetProductionTicketsRequest request = GetProductionTicketsRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(ProductionTicketFilter.newBuilder().setTicketId(ticketId))
                .setPageIndex(0)
                .setPageSize(1).build();
        GetProductionTicketsResponse response = m_ProductionTicketGrpcService.getProductionTickets(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        log.info("Request id: " + request.getRequestContext().getRequestId()
                + "getProductionTicketById request sent through getProductionTicket grpc request to production-ticket-service sent");

        ResponseContext responseContext = response.getResponseContext();
        if (responseContext.hasError()) {
            throw new Exception(responseContext.getError().getErrorMessage());
        }

        return response.getProductionTicketList().isEmpty() ? Optional.empty() : Optional.ofNullable(m_productionTicketConverter.protoToDto(response.getProductionTicketList().get(0)));
    }
}
