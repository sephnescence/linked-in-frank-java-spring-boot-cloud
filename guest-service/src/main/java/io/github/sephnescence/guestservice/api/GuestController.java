package io.github.sephnescence.guestservice.api;

import io.github.sephnescence.guestservice.data.Guest;
import io.github.sephnescence.guestservice.data.GuestRepository;
import io.github.sephnescence.guestservice.error.BadReqeustException;
import io.github.sephnescence.guestservice.error.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/guests")
public class GuestController {

    private final GuestRepository guestRepository;

    public GuestController(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }


    @GetMapping
    public Iterable<Guest> getGuests(@RequestParam(value = "emailAddress", required = false) String emailAddress) {
        if (StringUtils.hasLength(emailAddress)) {
            return this.guestRepository.findGuestsByEmailAddress(emailAddress);
        }
        return this.guestRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Guest addGuest(@RequestBody Guest guest) {
        return this.guestRepository.save(guest);
    }

    @GetMapping("/{id}")
    public Guest getGuest(@PathVariable("id") Long id) {
        Optional<Guest> guest = this.guestRepository.findById(id);
        if (guest.isEmpty()) {
            throw new NotFoundException("id not found: " + id);
        }
        return guest.get();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateGuest(@PathVariable("id") Long id, @RequestBody Guest guest) {
        if (id != guest.getGuestId()) {
            throw new BadReqeustException("incoming id in body doesn't match path");
        }
        this.guestRepository.save(guest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteGuest(@PathVariable("id") Long id) {
        this.guestRepository.deleteById(id);
    }
}
