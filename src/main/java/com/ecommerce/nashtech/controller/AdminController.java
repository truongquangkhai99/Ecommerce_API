package com.ecommerce.nashtech.controller;

import com.ecommerce.nashtech.dto.GraphQLRequest;
import com.ecommerce.nashtech.dto.order.OrderResponse;
import com.ecommerce.nashtech.dto.perfume.PerfumeRequest;
import com.ecommerce.nashtech.dto.perfume.PerfumeResponse;
import com.ecommerce.nashtech.dto.user.UserRequest;
import com.ecommerce.nashtech.dto.user.UserResponse;
import com.ecommerce.nashtech.exception.InputFieldException;
import com.ecommerce.nashtech.mapper.OrderMapper;
import com.ecommerce.nashtech.mapper.PerfumeMapper;
import com.ecommerce.nashtech.mapper.UserMapper;
import com.ecommerce.nashtech.service.graphql.GraphQLProvider;
import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserMapper userMapper;
    private final PerfumeMapper perfumeMapper;
    private final OrderMapper orderMapper;
    private final GraphQLProvider graphQLProvider;

    @PostMapping("/add")
    public ResponseEntity<PerfumeResponse> addPerfume(@RequestPart(name = "file", required = false) MultipartFile file,
                                                      @RequestPart("perfume") @Valid PerfumeRequest perfume,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return ResponseEntity.ok(perfumeMapper.savePerfume(perfume, file));
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<PerfumeResponse> updatePerfume(@RequestPart(name = "file", required = false) MultipartFile file,
                                                         @RequestPart("perfume") @Valid PerfumeRequest perfume,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return ResponseEntity.ok(perfumeMapper.savePerfume(perfume, file));
        }
    }

    @DeleteMapping("/delete/{perfumeId}")
    public ResponseEntity<List<PerfumeResponse>> deletePerfume(@PathVariable(value = "perfumeId") Long perfumeId) {
        return ResponseEntity.ok(perfumeMapper.deleteOrder(perfumeId));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderMapper.findAllOrders());
    }

    @PostMapping("/order")
    public ResponseEntity<List<OrderResponse>> getUserOrdersByEmail(@RequestBody UserRequest user) {
        return ResponseEntity.ok(orderMapper.findOrderByEmail(user.getEmail()));
    }

    @DeleteMapping("/order/delete/{orderId}")
    public ResponseEntity<List<OrderResponse>> deleteOrder(@PathVariable(value = "orderId") Long orderId) {
        return ResponseEntity.ok(orderMapper.deleteOrder(orderId));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userMapper.findUserById(userId));
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userMapper.findAllUsers());
    }

    @PostMapping("/graphql/user")
    public ResponseEntity<ExecutionResult> getUserByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping("/graphql/user/all")
    public ResponseEntity<ExecutionResult> getAllUsersByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping("/graphql/orders")
    public ResponseEntity<ExecutionResult> getAllOrdersQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping("/graphql/order")
    public ResponseEntity<ExecutionResult> getUserOrdersByEmailQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }
}
