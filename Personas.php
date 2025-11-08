<?php
class Personas
{

    private $conexion;
    private $table = "personas";

    public $id;
    public $nombres;
    public $telefono;
    public $latitud;
    public $longitud;
    public $firma;

    // Constructor de la clase personas
    public function __construct($db)
    {
        $this->conexion = $db;
    }


    // Create
    public function createPerson()
    {
        $consulta = "INSERT INTO 
                    " . $this->table . "
                    SET 
                    nombres = :nombres,
                    telefono = :telefono,
                    latitud = :latitud,
                    longitud = :longitud,
                    firma = :firma ";

        $comando = $this->conexion->prepare($consulta);

        // Sanitizacion
        $this->nombres = htmlspecialchars(strip_tags($this->nombres));
        $this->telefono = htmlspecialchars(strip_tags($this->telefono));
        $this->latitud = htmlspecialchars(strip_tags($this->latitud));
        $this->longitud = htmlspecialchars(strip_tags($this->longitud));
        $this->firma = htmlspecialchars(strip_tags($this->firma));

        // bind data
        $comando->bindParam(":nombres", $this->nombres);
        $comando->bindParam(":telefono", $this->telefono);
        $comando->bindParam(":latitud", $this->latitud);
        $comando->bindParam(":longitud", $this->longitud);
        $comando->bindParam(":firma", $this->firma);

        if($comando->execute())
        {
            return true;
        }
        return false;
    }

    // Read
     
    public function GetListPersons()
    {
        $consulta = "SELECT * FROM " . $this->table . "";
        $comando = $this->conexion->prepare($consulta);
        $comando->execute();

        return $comando;
    }

    // Update
    public function updatePerson()
    {
        $consulta = "UPDATE " . $this->table . " SET
                    nombres = :nombres,
                    telefono = :telefono,
                    latitud = :latitud,
                    longitud = :longitud
                    WHERE id = :id";

        $comando = $this->conexion->prepare($consulta);

        // Sanitizacion
        $this->id = htmlspecialchars(strip_tags($this->id));
        $this->nombres = htmlspecialchars(strip_tags($this->nombres));
        $this->telefono = htmlspecialchars(strip_tags($this->telefono));
        $this->latitud = htmlspecialchars(strip_tags($this->latitud));
        $this->longitud = htmlspecialchars(strip_tags($this->longitud));

        // bind data
        $comando->bindParam(":nombres", $this->nombres);
        $comando->bindParam(":telefono", $this->telefono);
        $comando->bindParam(":latitud", $this->latitud);
        $comando->bindParam(":longitud", $this->longitud);
        $comando->bindParam(":id", $this->id);

        if ($comando->execute()) {
            return true;
        }
        return false;
    }

    public function deletePerson()
    {
        $consulta = "DELETE FROM " . $this->table . " WHERE id = :id";

        $comando = $this->conexion->prepare($consulta);

        // Sanitización
        $this->id = htmlspecialchars(strip_tags($this->id));

        // Binding
        $comando->bindParam(':id', $this->id);

         if($comando->execute())
        {
            return true;
        }
        return false;
    }
}


?>