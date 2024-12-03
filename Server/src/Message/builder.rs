use super::error_message::ErrorMessage;
use super::move_message::MoveMessage;
use super::traits::Message;

pub struct MessageBuilder
{
    kind: Option<String>;
    error_code: Option<u16>;
    error_message: Option<String>;
    from: Option<(u8, u8)>;
    to: Option<(u8, u8)>;
}

impl MessageBuilder
{
    pub fn new() -> Self
    {
        Self
        {
            kind: None,
            error_code: None,
            error_message: None,
            from: None,
            to: None,
        }
    }

    pub fn kind(mut self, kind: &str) -> Self
    {
        self.kind = Some(kind.to_string());
        return self
    }

    pub fn error_code(mut self, error_code: u16) -> Self
    {
        self.error_code = Some(code);
        return self
    }

    pub fn error_message(mut self, error_message: &str) -> Self
    {
        self.error_message = Some(message.to_string());
        return self
    }

    pub fn from(mut self, from: (u8, u8)) -> Self
    {
        self.from = Some(from);
        return self
    }

    pub fn to(mut self, to: (u8, u8)) -> Self
    {
        self.to = Some(to);
        return self
    }

    pub fn build(self) -> Box<dyn Message> //zwraca wartość Box, przechowującą dynamicznie alokowaną implementacje traitu message
    {
        match self.kind.as_deref() //as_deref() zwraca Option<&str> zamiast Option<String>
        {
            Some("error") => Box::new(ErrorMessage
            {
                code: self.error_code.expect("Brak kodu bledu"),
                message: self.error_message.expect("Brak opisu bledu"),
            }),
            Some("move") => Box::new(MoveMessage
            {
                from: self.from.expect("Brak pola from"),
                to: self.to.expect("Brak pola to"),
            }),
            _ => panic!("Nieznany typ wiadomości"),
        }
    }

