package models;

import java.net.InetAddress;
import java.util.List;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "unauthed_users_listings")
public class UnauthedUserListing extends Model {
	
	@Column(name = "IPv4_address")
    public long ipAddress;
	
	@ManyToOne
	@JoinColumn(name="rental_listing_id")
    public RentalListing rentalListing;
    
    public UnauthedUserListing(InetAddress address, RentalListing rentalListing) {
        this.ipAddress = inetToLong(address);
        this.rentalListing = rentalListing;
    }
    
    public static List<UnauthedUserListing> findByIp(InetAddress address) {
    	return find("byIpAddress", inetToLong(address)).fetch();
    }
    
    private static long inetToLong(InetAddress address) {
    	/**
    	 * .getaddress() returns the raw IP address of this InetAddress object. 
    	 * The result is in network byte order: the highest order byte 
    	 * of the address is in getAddress()[0].
    	 * Big-endian --> first byte is MOST significant
    	 */
    	byte[] bytes = address.getAddress();
    	long value = 0;
    	for (int i = 0; i < bytes.length; i++) {
    		value = (value << 8) + (bytes[i] & 0xff);
    	}
    	return value;
    }
}
