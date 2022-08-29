package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.ReaderCardDTO;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCard;
import com.example.intern_vnpttech_libmanagement.services.ReaderCardService;
import com.example.intern_vnpttech_libmanagement.services.ReaderCardTypeService;
import com.example.intern_vnpttech_libmanagement.services.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("libmng/api/card")
@Tag(name = " ReaderCard Controller")
public class ReaderCardController {

    @Autowired
    private ReaderCardService readerCardService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private ReaderCardTypeService readerCardTypeService;

    @Operation(summary = "Find a reader's card")
    @GetMapping(path = "/find-by-reader")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findByReader(@RequestParam(name = "reader_id")long readerId)
    {
        if(!readerService.findById(readerId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"));
        return readerCardService.findByReader(readerId).isPresent()
                ?ResponseEntity.status(200).body(readerCardService.findByReader(readerId))
                :ResponseEntity.status(200).body(new MessageResponse("Card not found","fail"));
    }

    @GetMapping(path = "/publish")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> publish(@RequestParam(name = "reader_id") long readerId,
                                     @RequestParam(name = "card_type") long cardTypeId)
    {
        ReaderCard card = new ReaderCard();
        if(readerCardService.findByReader(readerId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("A card of this reader is existing","fail"));
        if(!readerCardTypeService.findById(cardTypeId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Card type not found","fail"));
        card.setCardType(readerCardTypeService.findById(cardTypeId).get());
        return readerService.findById(readerId).map(reader -> {
            card.setReader(reader);
            return readerCardService.publish(card).isPresent()
                    ?ResponseEntity.status(201).body(new MessageResponse("Publish the card successfully","success"))
                    :ResponseEntity.status(200).body(new MessageResponse("Publish the card fail","fail"));
        }).orElse(ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail")));
    }

    // update card api( include extend the card expiration)
    @Operation(summary = "Upadate card infos (include extend the card expiration)")
    @PutMapping(path = "/update")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestBody ReaderCardDTO cardDTO,
                                    @RequestParam(name = "card_id") long cardId)
    {
        if(!readerCardService.findByCardId(cardId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Card not found","fail"));
        else cardDTO.setCardId(cardId);
        return readerCardService.update(cardDTO).isPresent()
                ?ResponseEntity.status(201).body(new MessageResponse("Update card successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Update card fail","fail"));
    }

    @Operation(summary = "Delete a reader's card")
    @DeleteMapping(path = "/delete")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@RequestParam(name = "card_id") long cardId)
    {
        return readerCardService.delete(cardId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete the card successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete the card fail","fail"));
    }

}
