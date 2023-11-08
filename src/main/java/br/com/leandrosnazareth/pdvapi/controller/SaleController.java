package br.com.leandrosnazareth.pdvapi.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.leandrosnazareth.pdvapi.config.SpringFoxConfig;
import br.com.leandrosnazareth.pdvapi.dto.SaleDto;
import br.com.leandrosnazareth.pdvapi.exception.ResourceNotFoundException;
import br.com.leandrosnazareth.pdvapi.service.SaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/pdv/sale")
@Api(tags = { SpringFoxConfig.SALE_TAG })
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping("{id}")
    @ApiOperation(value = "Buscar venda pelo ID")
    public ResponseEntity<SaleDto> findSaleById(@PathVariable Long id)
            throws ResourceNotFoundException {
        // retornar um Optional<saleDto> e converte para saleDto, em caso nulo
        SaleDto saleDto = saleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado uma venda com id: " + id));
        return ResponseEntity.ok().body(saleDto);
    }

    @PostMapping
    @ApiOperation(value = "Salva uma venda")
    public SaleDto createSale(@Valid @RequestBody SaleDto saleDto) {
        return saleService.save(saleDto);
    }

    @ApiOperation(value = "Listar todas vendas ativas com paginação")
    @GetMapping
    public Page<SaleDto> findAllPagination(
            @RequestParam(value = "page", defaultValue = "0") Integer pagina,
            @RequestParam(value = "size", defaultValue = "5") Integer tamanhoPagina) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina);
        return saleService.findAll(pageRequest);
    }

    @ApiOperation(value = "Deletar venda pelo ID")
    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteById(@PathVariable Long id)
            throws ResourceNotFoundException {
        SaleDto saleDto = saleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi encontrada uma venda com id: " + id));
        saleService.delete(saleDto);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualizar venda")
    public ResponseEntity<SaleDto> updateSale(@PathVariable(value = "id") Long id,
                                              @Valid @RequestBody SaleDto saleDto) throws ResourceNotFoundException {
        saleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Não foi encontrada uma venda com id: " + saleDto.getId()));
        return ResponseEntity.ok(saleService.update(saleDto,id));
    }

    @GetMapping("total/{createdAt}")
    @ApiOperation(value = "Listar valor total de vendas no mes")
    public BigDecimal findValorTotalMonthAndYear2(@Valid @RequestBody LocalDateTime createdAt) {
        return saleService.findValorTotalMonthAndYear2(createdAt);
    }

    @GetMapping("somatotal")
    @ApiOperation(value = "Listar valor total de vendas")
    public BigDecimal findValorTotalSales() {
        return saleService.findValorTotalSales();
    }

    @ApiOperation(value = "Retornar quantidade de vendas")
    @GetMapping("count")
    public long countSales() {
        return saleService.countSales();
    }

    @ApiOperation(value = "Retornar ultimas 10 vendas")
    @GetMapping("limit")
    public List<SaleDto> findTopByOrderByIdDesc() {
        return saleService.findTopByOrderByIdDesc();
    }
}
